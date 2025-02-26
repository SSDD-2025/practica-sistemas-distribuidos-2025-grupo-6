package es.dlj.onlinestore.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.enumeration.ProductType;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.ProductTag;
import es.dlj.onlinestore.repository.ProductRepository;
import es.dlj.onlinestore.repository.ProductTagRepository;
import es.dlj.onlinestore.repository.UserInfoRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

    private Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository products;

    @Autowired
    private ProductTagRepository productTags;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserService userService;
        
    @PostConstruct
    @Transactional
    public void init() {
        List<Product> productList = List.of(
            new Product("Laptop Dell XPS 15", 1500.99f, "High-end laptop", ProductType.NEW, 10, transformStringToTags("electronics, laptop"), 20),
            new Product("iPhone 13 Pro", 1200.99f, "Latest Apple smartphone", ProductType.NEW, 15, transformStringToTags("smartphone, apple"), 10),
            new Product("Samsung Galaxy S21", 1000.99f, "Samsung flagship phone", ProductType.RECONDITIONED, 20, transformStringToTags("smartphone, android"), 15),
            new Product("HP Pavilion 14", 750.99f, "Affordable HP laptop", ProductType.SECONDHAND, 12, transformStringToTags("laptop, hp"), 5),
            new Product("MacBook Air M1", 999.99f, "Apple M1 laptop", ProductType.NEW, 8, transformStringToTags("laptop, apple"), 25),
            new Product("PlayStation 5", 499.99f, "Next-gen gaming console", ProductType.NEW, 5, transformStringToTags("gaming, console"), 30),
            new Product("Xbox Series X", 499.99f, "Microsoft gaming console", ProductType.RECONDITIONED, 6, transformStringToTags("gaming, console"), 20),
            new Product("iPad Air 4", 599.99f, "Apple tablet", ProductType.NEW, 10, transformStringToTags("tablet, apple"), 10),
            new Product("Kindle Paperwhite", 150.99f, "Amazon e-reader", ProductType.SECONDHAND, 20, transformStringToTags("tablet, reader"), 5),
            new Product("Sony WH-1000XM4", 350.99f, "Noise-canceling headphones", ProductType.NEW, 15, transformStringToTags("audio, headphones"), 25),
            new Product("Bose QC35 II", 299.99f, "Wireless headphones", ProductType.RECONDITIONED, 12, transformStringToTags("audio, headphones"), 15),
            new Product("LG OLED CX 55", 1300.99f, "55-inch OLED TV", ProductType.NEW, 8, transformStringToTags("tv, lg"), 20),
            new Product("Samsung QLED Q80T", 1200.99f, "65-inch QLED TV", ProductType.NEW, 10, transformStringToTags("tv, samsung"), 10),
            new Product("Nikon D3500", 450.99f, "DSLR Camera", ProductType.SECONDHAND, 7, transformStringToTags("camera, nikon"), 5),
            new Product("Canon EOS M50", 600.99f, "Mirrorless Camera", ProductType.NEW, 5, transformStringToTags("camera, canon"), 20),
            new Product("GoPro Hero 9", 400.99f, "Action Camera", ProductType.RECONDITIONED, 10, transformStringToTags("camera, gopro"), 15),
            new Product("Surface Pro 7", 800.99f, "Microsoft tablet-laptop hybrid", ProductType.NEW, 9, transformStringToTags("tablet, microsoft"), 10),
            new Product("Dell UltraSharp 27", 500.99f, "4K Monitor", ProductType.NEW, 11, transformStringToTags("monitor, dell"), 25),
            new Product("Apple Watch Series 7", 399.99f, "Smartwatch", ProductType.NEW, 14, transformStringToTags("watch, apple"), 20),
            new Product("Samsung Galaxy Watch 4", 299.99f, "Smartwatch", ProductType.RECONDITIONED, 13, transformStringToTags("watch, samsung"), 15)
        );

        List<List<String>> images = List.of(
            List.of("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTmeFCPecfZoPQuMqnIbtnVxWt8iePZ2WHsYQ&s", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3iIUP3Ec_Jh_pBg1DblZ_NhmIoCEHGGtzFw&s")
            
        );

        for (int i = 0; i < productList.size(); i++) {
            try {
                imageService.saveImagesFromHttp(productList.get(i), images.get(0));
            } catch (IOException e) {}
            if (i < 5) {
                productList.get(i).setSeller(userService.findById(1L));
            }
            products.save(productList.get(i));
            if (i < 5) {
                userService.findById(1L).addProduct(productList.get(i));
            }
        }

    }

    public void updateProduct(Long id, Product updatedProduct) {
        Product product = getProduct(id);
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setDescription(updatedProduct.getDescription());
        product.setProductType(updatedProduct.getProductType());
        product.setStock(updatedProduct.getStock());
        product.setSale(updatedProduct.getSale());
        product.setTags(updatedProduct.getTags());
        products.save(product);
    }

    public Product saveProduct(Product product) {
        return products.save(product);
    }

    public List<ProductTag> transformStringToTags(String tagsAsString){
        List<ProductTag> tagList = new ArrayList<>();
        for (String tag: tagsAsString.split(",")){
            tag = tag.trim();
            ProductTag productTag;
            if (productTags.existsByName(tag)) {
                productTag = productTags.findByName(tag);
            } else {
                productTag = new ProductTag(tag);
                productTags.save(productTag);
            }
            tagList.add(productTag);
        }
        return tagList;
    }

    /**
     * Gets all tags string and the number of products that have that tag:
     * [{name: "tag1", count: 5}, {name: "tag2", count: 3}, ...]
     * @return List of maps with the tag name and the number of products that have that tag
     */
    public List<Map<String, Object>> getAllTags() {
        return productTags.findAllWithProductCount();
    }

    public List<Map<String, Object>> getAllProductTypesAndCount(ProductType selected) {
        List<Map<String, Object>> productTypesList = new ArrayList<>();
        for (ProductType type : ProductType.values()) {
            productTypesList.add(Map.of("name", type.toString(), 
                                        "count", products.countByProductType(type),
                                        "selected", (selected != null && type.equals(selected))));
        }
        return productTypesList;
    }

    public List<Map<String, Object>> getAllProductTypesAndCount() {
        return getAllProductTypesAndCount(null);
    }

    public List<Product> getAllProducts() {
        return products.findAll();
    }

    public List<Product> getProductsByProductType(ProductType type) {
        return products.findByProductType(type);
    }

    public List<Product> findByNameContaining(String name) {
        return products.findByNameContaining(name);
    }

    public Product getProduct(long id) {
        return products.findById(id).get();
    }

    public List<Product> searchProducts(String name, Integer minPrice, Integer maxPrice, List<String> tags, List<String> productTypeStrings) {
        // Transform productTypeStrings to ProductType
        List<ProductType> productTypes = null;
        if (productTypeStrings != null) {
            productTypes = new ArrayList<>();
            for (String type : productTypeStrings) {
                productTypes.add(ProductType.valueOf(type));
            }
        }

        return products.searchProducts(name, minPrice, maxPrice, tags, productTypes);
    }

    // Best Sellers
    public List<Product> getBestSellers() {
        return products.findTop10ByOrderByTotalSellsDesc();
    }

    // Top Rated
    public List<Product> getTopRated() {
        return products.findTop10ByOrderByRatingDesc();
    }

    // On Sale
    public List<Product> getOnSale(int sale) {
        return products.findBySaleGreaterThan(sale);
    }

    // Trending This Week
    public List<Product> getTrendingThisWeek() {
        return products.findTop10ByOrderByLastWeekSellsDesc();
    }

    // Low Stock
    public List<Product> getLowStock(int stock) {
        return products.findByStockLessThan(stock);
    }

    public Optional<Product> findById(long id){
        return products.findById(id);
    }

    public void addRatingToProduct(Product product, int rating) {
        product.addRating(rating);
        products.save(product);
    }

}