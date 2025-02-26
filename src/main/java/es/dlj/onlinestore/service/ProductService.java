package es.dlj.onlinestore.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.enumeration.ProductType;
import es.dlj.onlinestore.model.Image;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.ProductTag;
import es.dlj.onlinestore.repository.ProductRepository;
import es.dlj.onlinestore.repository.ProductTagRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTagRepository productTags;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @PostConstruct
    @Transactional
    public void init() {
        List<Product> productList = List.of(
            productRepository.save(new Product("Laptop Dell XPS 15", 1500.99f, "High-end laptop", ProductType.NEW, 10, transformStringToTags("electronics, laptop"), 20)),
            productRepository.save(new Product("iPhone 13 Pro", 1200.99f, "Latest Apple smartphone", ProductType.NEW, 15, transformStringToTags("smartphone, apple"), 10)),
            productRepository.save(new Product("Samsung Galaxy S21", 1000.99f, "Samsung flagship phone", ProductType.RECONDITIONED, 20, transformStringToTags("smartphone, android"), 15)),
            productRepository.save(new Product("HP Pavilion 14", 750.99f, "Affordable HP laptop", ProductType.SECONDHAND, 12, transformStringToTags("laptop, hp"), 5)),
            productRepository.save(new Product("MacBook Air M1", 999.99f, "Apple M1 laptop", ProductType.NEW, 8, transformStringToTags("laptop, apple"), 25)),
            productRepository.save(new Product("PlayStation 5", 499.99f, "Next-gen gaming console", ProductType.NEW, 5, transformStringToTags("gaming, console"), 30)),
            productRepository.save(new Product("Xbox Series X", 499.99f, "Microsoft gaming console", ProductType.RECONDITIONED, 6, transformStringToTags("gaming, console"), 20)),
            productRepository.save(new Product("iPad Air 4", 599.99f, "Apple tablet", ProductType.NEW, 10, transformStringToTags("tablet, apple"), 10)),
            productRepository.save(new Product("Kindle Paperwhite", 150.99f, "Amazon e-reader", ProductType.SECONDHAND, 20, transformStringToTags("tablet, reader"), 5)),
            productRepository.save(new Product("Sony WH-1000XM4", 350.99f, "Noise-canceling headphones", ProductType.NEW, 15, transformStringToTags("audio, headphones"), 25)),
            productRepository.save(new Product("Bose QC35 II", 299.99f, "Wireless headphones", ProductType.RECONDITIONED, 12, transformStringToTags("audio, headphones"), 15)),
            productRepository.save(new Product("LG OLED CX 55", 1300.99f, "55-inch OLED TV", ProductType.NEW, 8, transformStringToTags("tv, lg"), 20)),
            productRepository.save(new Product("Samsung QLED Q80T", 1200.99f, "65-inch QLED TV", ProductType.NEW, 10, transformStringToTags("tv, samsung"), 10)),
            productRepository.save(new Product("Nikon D3500", 450.99f, "DSLR Camera", ProductType.SECONDHAND, 7, transformStringToTags("camera, nikon"), 5)),
            productRepository.save(new Product("Canon EOS M50", 600.99f, "Mirrorless Camera", ProductType.NEW, 5, transformStringToTags("camera, canon"), 20)),
            productRepository.save(new Product("GoPro Hero 9", 400.99f, "Action Camera", ProductType.RECONDITIONED, 10, transformStringToTags("camera, gopro"), 15)),
            productRepository.save(new Product("Surface Pro 7", 800.99f, "Microsoft tablet-laptop hybrid", ProductType.NEW, 9, transformStringToTags("tablet, microsoft"), 10)),
            productRepository.save(new Product("Dell UltraSharp 27", 500.99f, "4K Monitor", ProductType.NEW, 11, transformStringToTags("monitor, dell"), 25)),
            productRepository.save(new Product("Apple Watch Series 7", 399.99f, "Smartwatch", ProductType.NEW, 14, transformStringToTags("watch, apple"), 20)),
            productRepository.save(new Product("Samsung Galaxy Watch 4", 299.99f, "Smartwatch", ProductType.RECONDITIONED, 13, transformStringToTags("watch, samsung"), 15))
        );

        List<List<String>> productImages = new ArrayList<>();
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));

        for (int i = 0; i < productList.size(); i++) {
            try {
                for (String productImage : productImages.get(i)) {
                    Image thisImage = imageService.saveFileImageFromPath("src\\main\\resources\\static\\images\\preloaded\\" + productImage);
                    productList.get(i).addImage(thisImage);
                }
            } catch (IOException e) {}
            if (i < 5) {
                productList.get(i).setSeller(userService.findById(1L));
            }
            productRepository.save(productList.get(i));
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
        productRepository.save(product);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
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
                                        "count", productRepository.countByProductType(type),
                                        "selected", (selected != null && type.equals(selected))));
        }
        return productTypesList;
    }

    public List<Map<String, Object>> getAllProductTypesAndCount() {
        return getAllProductTypesAndCount(null);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByProductType(ProductType type) {
        return productRepository.findByProductType(type);
    }

    public List<Product> findByNameContaining(String name) {
        return productRepository.findByNameContaining(name);
    }

    public Product getProduct(long id) {
        return productRepository.findById(id).get();
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

        return productRepository.searchProducts(name, minPrice, maxPrice, tags, productTypes);
    }

    // Best Sellers
    public List<Product> getBestSellers() {
        return productRepository.findTop10ByOrderByTotalSellsDesc();
    }

    // Top Rated
    public List<Product> getTopRated() {
        return productRepository.findTop10ByOrderByRatingDesc();
    }

    // On Sale
    public List<Product> getOnSale(int sale) {
        return productRepository.findBySaleGreaterThan(sale);
    }

    // Trending This Week
    public List<Product> getTrendingThisWeek() {
        return productRepository.findTop10ByOrderByLastWeekSellsDesc();
    }

    // Low Stock
    public List<Product> getLowStock(int stock) {
        return productRepository.findByStockLessThan(stock);
    }

    public Optional<Product> findById(long id){
        return productRepository.findById(id);
    }

    public void addRatingToProduct(Product product, int rating) {
        product.addRating(rating);
        productRepository.save(product);
    }

}