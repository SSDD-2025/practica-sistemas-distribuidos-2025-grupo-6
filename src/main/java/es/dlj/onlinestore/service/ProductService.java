package es.dlj.onlinestore.service;

import java.awt.geom.Arc2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.dlj.onlinestore.enumeration.ProductType;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.ProductTag;
import es.dlj.onlinestore.repository.ProductRepository;
import es.dlj.onlinestore.repository.ProductTagRepository;
import es.dlj.onlinestore.repository.UserInfoRepository;
import jakarta.annotation.PostConstruct;

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
        
    @PostConstruct
    public void init() {
        products.save(new Product("Laptop Dell XPS 15", 1500f, "High-end laptop", ProductType.NEW, 10, transformStringToTags("electronics, laptop"), 20));
        products.save(new Product("iPhone 13 Pro", 1200f, "Latest Apple smartphone", ProductType.NEW, 15, transformStringToTags("smartphone, apple"), 10));
        products.save(new Product("Samsung Galaxy S21", 1000f, "Samsung flagship phone", ProductType.RECONDITIONED, 20, transformStringToTags("smartphone, android"), 15));
        products.save(new Product("HP Pavilion 14", 750f, "Affordable HP laptop", ProductType.SECONDHAND, 12, transformStringToTags("laptop, hp"), 5));
        products.save(new Product("MacBook Air M1", 999f, "Apple M1 laptop", ProductType.NEW, 8, transformStringToTags("laptop, apple"), 25));
        products.save(new Product("PlayStation 5", 499f, "Next-gen gaming console", ProductType.NEW, 5, transformStringToTags("gaming, console"), 30));
        products.save(new Product("Xbox Series X", 499f, "Microsoft gaming console", ProductType.RECONDITIONED, 6, transformStringToTags("gaming, console"), 20));
        products.save(new Product("iPad Air 4", 599f, "Apple tablet", ProductType.NEW, 10, transformStringToTags("tablet, apple"), 10));
        products.save(new Product("Kindle Paperwhite", 150f, "Amazon e-reader", ProductType.SECONDHAND, 20, transformStringToTags("tablet, reader"), 5));
        products.save(new Product("Sony WH-1000XM4", 350f, "Noise-canceling headphones", ProductType.NEW, 15, transformStringToTags("audio, headphones"), 25));
        products.save(new Product("Bose QC35 II", 299f, "Wireless headphones", ProductType.RECONDITIONED, 12, transformStringToTags("audio, headphones"), 15));
        products.save(new Product("LG OLED CX 55", 1300f, "55-inch OLED TV", ProductType.NEW, 8, transformStringToTags("tv, lg"), 20));
        products.save(new Product("Samsung QLED Q80T", 1200f, "65-inch QLED TV", ProductType.NEW, 10, transformStringToTags("tv, samsung"), 10));
        products.save(new Product("Nikon D3500", 450f, "DSLR Camera", ProductType.SECONDHAND, 7, transformStringToTags("camera, nikon"), 5));
        products.save(new Product("Canon EOS M50", 600f, "Mirrorless Camera", ProductType.NEW, 5, transformStringToTags("camera, canon"), 20));
        products.save(new Product("GoPro Hero 9", 400f, "Action Camera", ProductType.RECONDITIONED, 10, transformStringToTags("camera, gopro"), 15));
        products.save(new Product("Surface Pro 7", 800f, "Microsoft tablet-laptop hybrid", ProductType.NEW, 9, transformStringToTags("tablet, microsoft"), 10));
        products.save(new Product("Dell UltraSharp 27", 500f, "4K Monitor", ProductType.NEW, 11, transformStringToTags("monitor, dell"), 25));
        products.save(new Product("Apple Watch Series 7", 399f, "Smartwatch", ProductType.NEW, 14, transformStringToTags("watch, apple"), 20));
        products.save(new Product("Samsung Galaxy Watch 4", 299f, "Smartwatch", ProductType.RECONDITIONED, 13, transformStringToTags("watch, samsung"), 15));
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

    
    // public Product saveProduct(RawProduct rawProduct) {
    //     List<ProductTag> productTagsList = transformStringToTags(rawProduct.getTags());
    //     ProductType productType = ProductType.valueOf(rawProduct.getProductType());
    //     MultipartFile mainImage = rawProduct.getMainImage();
    //     List <MultipartFile> images = rawProduct.getImages();
    //     if (mainImage != null){
    //         images.addFirst(mainImage);
    //     }
    //     Product product = new Product(rawProduct.getName(), rawProduct.getPrice(), rawProduct.getDescription(), productType, rawProduct.getStock(), productTagsList, rawProduct.getSeller());
    //     products.save(product);
    //     boolean isMainImage = false;
    //     if (images != null && images.size() > 0){
    //         for (int i=1; i <= images.size(); i++){
    //             if (i == 1){
    //                 isMainImage = true;
    //             }
    //             log.info("imagen " + i);
    //             MultipartFile rawImage = images.get(i-1);
    //             log.info("imagen" + rawImage.getName());
    //             imageService.saveImage(product, rawImage, isMainImage);
    //             isMainImage = false;
    //         }
    //     }
    //     products.save(product);
    //     log.info("Id del vendedor: "+rawProduct.getSeller());
    //     if (product.getProductType() == ProductType.SECONDHAND){
    //         Optional <UserInfo> user = userInfoRepository.findById(rawProduct.getSeller());
    //         if (user.isPresent()){
    //             user.get().addProduct(product);
    //             log.info("Productos en venta_ " + user.get().getProductsForSell().size());
    //             userInfoRepository.save(user.get());
    //         }
    //     }
    //     return product;
    // }

    // public String checkForProductFormErrors(Product product){
    //     if (product.getName() == null){
    //         return "The product requires a name";
    //     }
    //     else if (product.getPrice() <= 0) {
    //         return "Price must be positive grater than 0.";
    //     }
    //     else if (product.getDescription() == null || product.getDescription().length()<200){
    //         return "Description needs to be 200 characters minimum.";
    //     }
    //     else if (product.getStock() <= 0) {
    //         return "Stock must be positive grater than 0.";
    //     } 
    //     else if(product.getMainImage() == null || product.getMainImage().isEmpty()){
    //         return "Add a main image for the product";
    //     }
    //      else if (product.getTags().size() <= 0){
    //         return "Product needs at least one tag.";
    //     } else if (product.getProductType() == null || (product.getProductType() != "NEW" & product.getProductType() != "RECONDITIONED" & product.getProductType() != "SECONDHAND")){
    //         return "Product Type not valid";
    //     }
    //     return null;
    // }

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

}