package es.dlj.onlinestore.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.ProductTag;
import es.dlj.onlinestore.model.ProductType;
import es.dlj.onlinestore.repository.ProductRepository;
import es.dlj.onlinestore.repository.ProductTagRepository;
import jakarta.annotation.PostConstruct;

@Service
public class ProductService {

    @Autowired
    private ProductRepository products;

    @Autowired
    private ProductTagRepository productTags;

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

        this.saveProduct("Laptop Dell XPS 15", 1500f, "High-end laptop", ProductType.NEW, 10, Arrays.asList("electronics", "laptop"));
        this.saveProduct("iPhone 13 Pro", 1200f, "Latest Apple smartphone", ProductType.NEW, 15, Arrays.asList("smartphone", "apple"));
        this.saveProduct("Samsung Galaxy S21", 1000f, "Samsung flagship phone", ProductType.NEW, 20, Arrays.asList("smartphone", "android"));
        this.saveProduct("HP Pavilion 14", 750f, "Affordable HP laptop", ProductType.NEW, 12, Arrays.asList("laptop", "hp"));
        this.saveProduct("MacBook Air M1", 999f, "Apple M1 laptop", ProductType.NEW, 8, Arrays.asList("laptop", "apple"));
        this.saveProduct("PlayStation 5", 499f, "Next-gen gaming console", ProductType.NEW, 5, Arrays.asList("gaming", "console"));
        this.saveProduct("Xbox Series X", 499f, "Microsoft gaming console", ProductType.NEW, 6, Arrays.asList("gaming", "console"));
        this.saveProduct("iPad Air 4", 599f, "Apple tablet", ProductType.NEW, 10, Arrays.asList("tablet", "apple"));
        this.saveProduct("Kindle Paperwhite", 150f, "Amazon e-reader", ProductType.NEW, 20, Arrays.asList("tablet", "reader"));
        this.saveProduct("Sony WH-1000XM4", 350f, "Noise-canceling headphones", ProductType.NEW, 15, Arrays.asList("audio", "headphones"));
        this.saveProduct("Bose QC35 II", 299f, "Wireless headphones", ProductType.NEW, 12, Arrays.asList("audio", "headphones"));
        this.saveProduct("LG OLED CX 55", 1300f, "55-inch OLED TV", ProductType.NEW, 8, Arrays.asList("tv", "lg"));
        this.saveProduct("Samsung QLED Q80T", 1200f, "65-inch QLED TV", ProductType.NEW, 10, Arrays.asList("tv", "samsung"));
        this.saveProduct("Nikon D3500", 450f, "DSLR Camera", ProductType.NEW, 7, Arrays.asList("camera", "nikon"));
        this.saveProduct("Canon EOS M50", 600f, "Mirrorless Camera", ProductType.NEW, 5, Arrays.asList("camera", "canon"));
        this.saveProduct("GoPro Hero 9", 400f, "Action Camera", ProductType.NEW, 10, Arrays.asList("camera", "gopro"));
        this.saveProduct("Surface Pro 7", 800f, "Microsoft tablet-laptop hybrid", ProductType.NEW, 9, Arrays.asList("tablet", "microsoft"));
        this.saveProduct("Dell UltraSharp 27", 500f, "4K Monitor", ProductType.NEW, 11, Arrays.asList("monitor", "dell"));
        this.saveProduct("Apple Watch Series 7", 399f, "Smartwatch", ProductType.NEW, 14, Arrays.asList("watch", "apple"));
        this.saveProduct("Samsung Galaxy Watch 4", 299f, "Smartwatch", ProductType.NEW, 13, Arrays.asList("watch", "samsung"));
        this.saveProduct("Apple Watch Series 7", 399f, "Smartwatch", ProductType.NEW, 14, Arrays.asList("watch", "apple"));
        this.saveProduct("Samsung Galaxy Watch 4", 299f, "Smartwatch", ProductType.NEW, 13, Arrays.asList("watch", "samsung"));


    }


    public void saveProduct(String name, float price, String description, ProductType productType, int stock, List<String> tags) {
        List<ProductTag> productTagsList = new ArrayList<>();
        for (String tag : tags) {
            ProductTag productTag;
            if (productTags.existsByName(tag)) {
                productTag = productTags.findByName(tag);
            } else {
                productTag = new ProductTag(tag);
                productTags.save(productTag);
            }
            productTagsList.add(productTag);
        }
        Product product = new Product(name, price, description, productType, stock, productTagsList);
        products.save(product);
    }

    /**
     * Gets all tags string and the number of products that have that tag:
     * [{name: "tag1", count: 5}, {name: "tag2", count: 3}, ...]
     * @return List of maps with the tag name and the number of products that have that tag
     */
    public List<Map<String, Object>> getAllTags() {
        return productTags.findAllWithProductCount();
    }

    public List<Map<String, Object>> getAllProductTypesAndCount() {
        List<Map<String, Object>> productTypesList = new ArrayList<>();
        for (ProductType type : ProductType.values()) {
            productTypesList.add(Map.of("name", type.toString(), "count", products.countByProductType(type)));
        }
        return productTypesList;
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

    public List<Product> searchProducts(String name, Integer minPrice, Integer maxPrice, List<String> tags, 
        List<String> productTypeStrings, String minSale, String maxSale, String minRating, String maxRating, 
        String minStock, String maxStock, String minWeekSells, String maxWeekSells, String minNumberRatings, 
        String maxNumberRatings, String minTotalSells, String maxTotalSells
    ) {
        // Transform productTypeStrings to ProductType
        List<ProductType> productTypes = null;
        if (productTypeStrings != null) {
            productTypes = new ArrayList<>();
            for (String type : productTypeStrings) {
                productTypes.add(ProductType.fromString(type));
            }
        }

        return products.searchProducts(name, minPrice, maxPrice, tags, productTypes, minSale, maxSale, minRating, maxRating, minStock, maxStock, minWeekSells, maxWeekSells, minNumberRatings, maxNumberRatings, minTotalSells, maxTotalSells);
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

}
