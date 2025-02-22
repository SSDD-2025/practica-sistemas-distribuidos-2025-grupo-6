package es.dlj.onlinestore.service;

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

    public class RawProduct{
        
        private String name;
        private float price;
        private String description;
        private int stock;
        private MultipartFile mainImage;
        private List<String> tags;
        private String productType;
        private List<MultipartFile>images;
                
        public RawProduct() {
            this.tags = new ArrayList<>();
            this.images = new ArrayList<>();
        }

        public String getName() {
            return name;
        }
    
        public float getPrice() {
            return price;
        }
    
        public String getDescription() {
            return description;
        }
    
        public int getStock() {
            return stock;
        }
    
        public MultipartFile getMainImage() {
            return mainImage;
        }
    
        public List<String> getTags() {
            return tags;
        }
    
        public String getProductType() {
            return productType;
        }
        
        public List<MultipartFile> getImages() {
            return this.images;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        
        public void setPrice(float price) {
            this.price = price;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
        
        public void setMainImage(MultipartFile mainImage) {
            this.mainImage = mainImage;
        }
        public void setTags(List<String> tags) {
            this.tags = tags;
        }
        
        public void setProductType(String productType) {
            this.productType = productType;
        }
        
        public void setImages(List<MultipartFile> images) {
            this.images = images;
        }
    }
        
            @PostConstruct
            public void init() {
                /*
                products.save(new Product("product1", 10.0f, "description1", Product.productType.NEW, 10, new LinkedList<>()));
                products.save(new Product("product2", 20.0f, "description2", Product.productType.NEW, 20, new LinkedList<>()));
                products.save(new Product("product3", 30.0f, "description3", Product.productType.NEW, 30, new LinkedList<>()));
                products.save(new Product("product4", 40.0f, "description4", Product.productType.RECONDITIONED, 40, new LinkedList<>()));
                products.save(new Product("product5", 50.0f, "description5", Product.productType.SECONDHAND, 50, new LinkedList<>()));
                products.save(new Product("product6", 60.0f, "description6", Product.productType.NEW, 60, new LinkedList<>()));
                */
        
                Product product = this.saveProduct("Laptop Dell XPS 15", 1500f, "High-end laptop", ProductType.NEW, 10, Arrays.asList("electronics", "laptop"), null, null);
                product.setSale(25f);
                products.save(product);
                this.saveProduct("iPhone 13 Pro", 1200f, "Latest Apple smartphone", ProductType.NEW, 15, Arrays.asList("smartphone", "apple"), null, null);
                this.saveProduct("Samsung Galaxy S21", 1000f, "Samsung flagship phone", ProductType.RECONDITIONED, 20, Arrays.asList("smartphone", "android"), null, null);
                this.saveProduct("HP Pavilion 14", 750f, "Affordable HP laptop", ProductType.NEW, 12, Arrays.asList("laptop", "hp"), null, null);
                this.saveProduct("MacBook Air M1", 999f, "Apple M1 laptop", ProductType.NEW, 8, Arrays.asList("laptop", "apple"), null, null);
                this.saveProduct("PlayStation 5", 499f, "Next-gen gaming console", ProductType.NEW, 5, Arrays.asList("gaming", "console"), null, null);
                this.saveProduct("Xbox Series X", 499f, "Microsoft gaming console", ProductType.NEW, 6, Arrays.asList("gaming", "console"), null, null);
                this.saveProduct("iPad Air 4", 599f, "Apple tablet", ProductType.NEW, 10, Arrays.asList("tablet", "apple"), null, null);
                this.saveProduct("Kindle Paperwhite", 150f, "Amazon e-reader", ProductType.NEW, 20, Arrays.asList("tablet", "reader"), null, null);
                this.saveProduct("Sony WH-1000XM4", 350f, "Noise-canceling headphones", ProductType.NEW, 15, Arrays.asList("audio", "headphones"), null, null);
                this.saveProduct("Bose QC35 II", 299f, "Wireless headphones", ProductType.NEW, 12, Arrays.asList("audio", "headphones"), null, null);
                this.saveProduct("LG OLED CX 55", 1300f, "55-inch OLED TV", ProductType.NEW, 8, Arrays.asList("tv", "lg"), null, null);
                this.saveProduct("Samsung QLED Q80T", 1200f, "65-inch QLED TV", ProductType.NEW, 10, Arrays.asList("tv", "samsung"), null, null);
                this.saveProduct("Nikon D3500", 450f, "DSLR Camera", ProductType.NEW, 7, Arrays.asList("camera", "nikon"), null, null);
                this.saveProduct("Canon EOS M50", 600f, "Mirrorless Camera", ProductType.NEW, 5, Arrays.asList("camera", "canon"), null, null);
                this.saveProduct("GoPro Hero 9", 400f, "Action Camera", ProductType.NEW, 10, Arrays.asList("camera", "gopro"), null, null);
                this.saveProduct("Surface Pro 7", 800f, "Microsoft tablet-laptop hybrid", ProductType.NEW, 9, Arrays.asList("tablet", "microsoft"), null, null);
                this.saveProduct("Dell UltraSharp 27", 500f, "4K Monitor", ProductType.NEW, 11, Arrays.asList("monitor", "dell"), null, null);
                this.saveProduct("Apple Watch Series 7", 399f, "Smartwatch", ProductType.NEW, 14, Arrays.asList("watch", "apple"), null, null);
                this.saveProduct("Samsung Galaxy Watch 4", 299f, "Smartwatch", ProductType.NEW, 13, Arrays.asList("watch", "samsung"), null, null);
                this.saveProduct("Apple Watch Series 7", 399f, "Smartwatch", ProductType.NEW, 14, Arrays.asList("watch", "apple"), null, null);
                this.saveProduct("Samsung Galaxy Watch 4", 299f, "Smartwatch", ProductType.NEW, 13, Arrays.asList("watch", "samsung"), null, null);
        
        
            }
        
            public Product editProduct(Long id, String name, float price, int sale, String description, ProductType productType, int stock, List<String> tags) throws IOException {
                Product product = getProduct(id);
                product.setTags(transformStringToTags(tags));
                product.setName(name);
                product.setPrice(price);
                product.setDescription(description);
                product.setProductType(productType);
                product.setStock(stock);
                product.setSale(sale);
                products.save(product);
                return product;
            }
        
            public Product saveProduct(Product product) {
                return products.save(product);
            }
    
    public Product saveProduct(RawProduct rawProduct) {
        List<ProductTag> productTagsList = transformStringToTags(rawProduct.getTags());
        ProductType productType = transformStringtoProductType(rawProduct.getProductType());
        MultipartFile mainImage = rawProduct.getMainImage();
        List <MultipartFile> images = rawProduct.getImages();
        if (mainImage != null){
            images.addFirst(mainImage);
        }
        Product product = new Product(rawProduct.getName(), rawProduct.getPrice(), rawProduct.getDescription(), productType, rawProduct.getStock(), productTagsList);
        products.save(product);
        boolean isMainImage = false;
        if (images != null && images.size() > 0){
            for (int i=1; i <= images.size(); i++){
                if (i == 1){
                    isMainImage = true;
                }
                log.info("imagen " + i);
                MultipartFile rawImage = images.get(i-1);
                log.info("imagen" + rawImage.getName());
                imageService.saveImage(product, rawImage, i, isMainImage);
                isMainImage = false;
            }
        }
        products.save(product);
        return product;
    }

    public Product saveProduct(String name, float price, String description, ProductType productType, int stock, List<String> tags, List<MultipartFile> rawImages, MultipartFile rawMainImage) {
        List<ProductTag> productTagsList = transformStringToTags(tags);
        if (rawMainImage != null){
            rawImages.addFirst(rawMainImage);
        }
        Product product = new Product(name, price, description, productType, stock, productTagsList);
        if (rawImages!= null){
            for (int i=1; i >= rawImages.size()+1; i++){
                MultipartFile rawImage = rawImages.get(i);
                imageService.saveImage(product, rawImage, i, false);
            }
        }
        products.save(product);
        return product;
    }

    public String checkForProductFormErrors(RawProduct product){
        if (product.getName() == null){
            return "The product requires a name";
        }
        else if (product.getPrice() <= 0) {
            return "Price must be positive grater than 0.";
        }
        else if (product.getDescription() == null || product.getDescription().length()<200){
            return "Description needs to be 200 characters minimum.";
        }
        else if (product.getStock() <= 0) {
            return "Stock must be positive grater than 0.";
        } 
        else if(product.getMainImage() == null || product.getMainImage().isEmpty()){
            return "Add a main image for the product";
        }
         else if (product.getTags().size() <= 0){
            return "Product needs at least one tag.";
        } else if (product.getProductType() == null || (product.getProductType() != "NEW" & product.getProductType() != "RECONDITIONED" & product.getProductType() != "SECONDHAND")){
            return "Product Type not valid";
        }
        return null;
    }

    public ProductType transformStringtoProductType(String productTypeString){
        return switch (productTypeString) {
            case "NEW" -> ProductType.NEW;
            case "RECONDITIONED" -> ProductType.RECONDITIONED;
            case "SECONDHAND" -> ProductType.SECONDHAND;
            default -> null;
        };
    }

    public List<ProductTag> transformStringToTags(List<String> tagsAsString){
        List<ProductTag> tagList = new ArrayList<>();
        for (String tag: tagsAsString){
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
                productTypes.add(ProductType.fromString(type));
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
