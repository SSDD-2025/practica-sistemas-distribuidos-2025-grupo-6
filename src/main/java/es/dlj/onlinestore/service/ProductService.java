package es.dlj.onlinestore.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.domain.Image;
import es.dlj.onlinestore.domain.Order;
import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.domain.ProductTag;
import es.dlj.onlinestore.domain.Review;
import es.dlj.onlinestore.domain.UserInfo;
import es.dlj.onlinestore.enumeration.ProductType;
import es.dlj.onlinestore.repository.OrderRepository;
import es.dlj.onlinestore.repository.ProductRepository;
import es.dlj.onlinestore.repository.ProductTagRepository;
import es.dlj.onlinestore.repository.UserInfoRepository;
import es.dlj.onlinestore.repository.UserReviewRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserReviewRepository reviewRepository;

    @Autowired
    private ProductTagRepository productTagRepository;

    @Autowired
    private OrderRepository orderRepository;
    
    @PostConstruct
    @Transactional
    public void init() {
        // Checks if there are products already in the database
        // if (productRepository.count() > 0) return;

        // Preload products
        List<Product> productList = List.of(
            productRepository.save(new Product("Laptop Dell XPS 15", 1500.99f, "High-end laptop", ProductType.NEW, 10, 20)),
            productRepository.save(new Product("iPhone 13 Pro", 1200.99f, "Latest Apple smartphone", ProductType.NEW, 15, 10)),
            productRepository.save(new Product("Samsung Galaxy S21", 1000.99f, "Samsung flagship phone", ProductType.RECONDITIONED, 20, 15)),
            productRepository.save(new Product("HP Pavilion 14", 750.99f, "Affordable HP laptop", ProductType.SECONDHAND, 12, 5)),
            productRepository.save(new Product("MacBook Air M1", 999.99f, "Apple M1 laptop", ProductType.NEW, 8, 25)),
            productRepository.save(new Product("PlayStation 5", 499.99f, "Next-gen gaming console", ProductType.NEW, 5, 30)),
            productRepository.save(new Product("Xbox Series X", 499.99f, "Microsoft gaming console", ProductType.RECONDITIONED, 6, 20)),
            productRepository.save(new Product("iPad Air 4", 599.99f, "Apple tablet", ProductType.NEW, 10, 10)),
            productRepository.save(new Product("Kindle Paperwhite", 150.99f, "Amazon e-reader", ProductType.SECONDHAND, 20, 5)),
            productRepository.save(new Product("Sony WH-1000XM4", 350.99f, "Noise-canceling headphones", ProductType.NEW, 15, 25)),
            productRepository.save(new Product("Bose QC35 II", 299.99f, "Wireless headphones", ProductType.RECONDITIONED, 12, 15)),
            productRepository.save(new Product("LG OLED CX 55", 1300.99f, "55-inch OLED TV", ProductType.NEW, 8, 20)),
            productRepository.save(new Product("Samsung QLED Q80T", 1200.99f, "65-inch QLED TV", ProductType.NEW, 10, 10)),
            productRepository.save(new Product("Nikon D3500", 450.99f, "DSLR Camera", ProductType.SECONDHAND, 7, 5)),
            productRepository.save(new Product("Canon EOS M50", 600.99f, "Mirrorless Camera", ProductType.NEW, 5, 20)),
            productRepository.save(new Product("GoPro Hero 9", 400.99f, "Action Camera", ProductType.RECONDITIONED, 10, 15)),
            productRepository.save(new Product("Surface Pro 7", 800.99f, "Microsoft tablet-laptop hybrid", ProductType.NEW, 9, 10)),
            productRepository.save(new Product("Dell UltraSharp 27", 500.99f, "4K Monitor", ProductType.NEW, 11, 25)),
            productRepository.save(new Product("Apple Watch Series 7", 399.99f, "Smartwatch", ProductType.NEW, 14, 20)),
            productRepository.save(new Product("Samsung Galaxy Watch 4", 299.99f, "Smartwatch", ProductType.RECONDITIONED, 13, 15))
        );

        // Preload images
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

        // Preload tags
        List<List<ProductTag>> productTagsList = new ArrayList<>();
        productTagsList.add(transformStringToTags("electronics, laptop"));
        productTagsList.add(transformStringToTags("smartphone, apple"));
        productTagsList.add(transformStringToTags("smartphone, android"));
        productTagsList.add(transformStringToTags("laptop, hp"));
        productTagsList.add(transformStringToTags("laptop, apple"));
        productTagsList.add(transformStringToTags("gaming, console"));
        productTagsList.add(transformStringToTags("gaming, console"));
        productTagsList.add(transformStringToTags("tablet, apple"));
        productTagsList.add(transformStringToTags("tablet, reader"));
        productTagsList.add(transformStringToTags("audio, headphones"));
        productTagsList.add(transformStringToTags("audio, headphones"));
        productTagsList.add(transformStringToTags("tv, lg"));
        productTagsList.add(transformStringToTags("tv, samsung"));
        productTagsList.add(transformStringToTags("camera, nikon"));
        productTagsList.add(transformStringToTags("camera, canon"));
        productTagsList.add(transformStringToTags("camera, gopro"));
        productTagsList.add(transformStringToTags("tablet, microsoft"));
        productTagsList.add(transformStringToTags("monitor, dell"));
        productTagsList.add(transformStringToTags("watch, apple"));
        productTagsList.add(transformStringToTags("watch, samsung"));

        // Preload products, tags and images
        UserInfo user = userService.findById(1L);
        for (int i = 0; i < productList.size(); i++) {

            for (ProductTag tag : productTagsList.get(i)) {
                
                tag.addProduct(productList.get(i));
                productTagRepository.save(tag);
            }
            productList.get(i).setTags(productTagsList.get(i));
            try {
                for (String productImage : productImages.get(i)) {
                    Image thisImage = imageService.saveFileImageFromPath("src/main/resources/static/images/preloaded/" + productImage);
                    productList.get(i).addImage(thisImage);
                }
            } catch (IOException e) {}
            if (i < 5) productList.get(i).setSeller(user);
            productRepository.save(productList.get(i));
        }
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public void updateProduct(Long id, Product updatedProduct) {
        Product product = getProduct(id);
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setDescription(updatedProduct.getDescription());
        product.setProductType(updatedProduct.getProductType());
        product.setStock(updatedProduct.getStock());
        product.setSale(updatedProduct.getSale());
        productRepository.save(product);
    }

    @Transactional
    public List<ProductTag> transformStringToTags(String tagsAsString){
        List<ProductTag> tagList = new ArrayList<>();
        for (String tag: tagsAsString.split(",")){
            tag = tag.trim();
            if (tag.isEmpty()) continue;
            ProductTag productTag;
            if (productTagRepository.existsByName(tag)) {
                productTag = productTagRepository.findByName(tag);
            } else {
                productTag = new ProductTag(tag);
                productTag = productTagRepository.save(productTag);
            }
            tagList.add(productTag);
        }
        return tagList;
    }

    public List<Map<String, Object>> getAllTags() {
        // Get all tags and the number of products that have that tag
        List<Map<String, Object>> tags = new ArrayList<>();
        for (ProductTag tag : productTagRepository.findAll()) {
            tags.add(Map.of("name", tag.getName(), "count", tag.getProducts().size()));
        }

        // Sort by count descending
        tags.sort((a, b) -> - ((Integer) a.get("count")).compareTo((Integer) b.get("count")));
        return tags;
    }

    public List<Map<String, Object>> getAllProductTypesAndCount(ProductType selected) {
        // Get all product types and the number of products that have that type
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

    public List<Product> getBestSellers() {
        return productRepository.findTop10ByOrderByTotalSellsDesc();
    }

    public List<Product> getBestSales() {
        return productRepository.findTop10ByOrderBySaleDesc();
    }

    public List<Product> getLowStock(int stock) {
        return productRepository.findByStockLessThan(stock);
    }

    public Optional<Product> findById(long id){
        return productRepository.findById(id);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return;
        deepDeleteImages(product);
        deepDeleteReviews(product);
        deepDeleteTags(product);
        deepDeleteSeller(product);
        deepDeleteOrders(product);
        productRepository.delete(product);
    }

    @Transactional
    private void deepDeleteImages(Product product) {
        List<Image> images = new ArrayList<>(product.getImages());
        for (Image image : images) {
            imageService.delete(image);
        }
        product.clearImages();
    }

    @Transactional
    private void deepDeleteSeller(Product product) {
        UserInfo seller = product.getSeller();
        if (seller != null) {
            seller.removeProductFromSale(product);
            userInfoRepository.save(seller);
        }
    }

    @Transactional
    private void deepDeleteOrders(Product product) {
        List<Order> orders = orderRepository.findByProductsContaining(product);
        for (Order order : orders) {
            order.removeProduct(product);
            orderRepository.save(order);
        }
    }

    @Transactional
    private void deepDeleteTags(Product product) {
        List<ProductTag> tags = product.getTags();
        for (ProductTag tag : tags) {
            tag.getProducts().remove(product);
            productTagRepository.save(tag);
        }
    }

    @Transactional
    private void deepDeleteReviews(Product product) {
        List<Review> reviews = new ArrayList<>(product.getReviews());
        for (Review review : reviews) {
            UserInfo owner = review.getOwner();
            if (owner != null) {
                owner.removeReview(review);
                userService.save(owner);
            }
            review.setProduct(null);
            product.getReviews().remove(review);
            reviewRepository.delete(review);
        }
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public ProductTag saveTag(ProductTag tag) {
        return productTagRepository.save(tag);
    }
}