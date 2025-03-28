package es.dlj.onlinestore.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.dlj.onlinestore.domain.Image;
import es.dlj.onlinestore.domain.Order;
import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.domain.ProductTag;
import es.dlj.onlinestore.domain.Review;
import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.dto.OrderDTO;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ProductSimpleDTO;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.enumeration.PaymentMethod;
import es.dlj.onlinestore.enumeration.ProductType;
import es.dlj.onlinestore.mapper.ProductMapper;
import es.dlj.onlinestore.mapper.UserMapper;
import es.dlj.onlinestore.repository.OrderRepository;
import es.dlj.onlinestore.repository.ProductRepository;
import es.dlj.onlinestore.repository.ProductTagRepository;
import es.dlj.onlinestore.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import es.dlj.onlinestore.repository.ReviewRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductTagRepository productTagRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserMapper userMapper;
    
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
        User user = userService.findUserById(1L);

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

    public ProductDTO findById(Long id) {
        return productMapper.toDTO(productRepository.findById(id).orElseThrow());
    }

    @Transactional
    public void updateProduct(Long id, ProductDTO newProductDTO, List<MultipartFile> imagesVal, String tagsVal) {
        Product newProduct = productMapper.toDomain(newProductDTO);
        Product oldProduct = productMapper.toDomain(getProduct(id));
        oldProduct.setName(newProduct.getName());
        oldProduct.setPrice(newProduct.getPrice());
        oldProduct.setDescription(newProduct.getDescription());
        oldProduct.setProductType(newProduct.getProductType());
        oldProduct.setStock(newProduct.getStock());
        oldProduct.setSale(newProduct.getSale());

        
        if (imagesVal != null && imagesVal.size() > 1) {
            try {
                imageService.saveImagesInProduct(newProduct, imagesVal);
            } catch (IOException e) {
                throw new NoSuchElementException("Error loading images"); 
            }
        }   

        List<ProductTag> oldTags = oldProduct.getTags();        
        Product savedProduct = save(oldProduct);
        savedProduct.getTags().clear();
        List<ProductTag> newTags = transformStringToTags(tagsVal);
        for (ProductTag tag : oldTags) {
            if (!newTags.contains(tag)) {
                tag.removeProduct(oldProduct);
                saveTag(tag);
            }
        }
    
        for (ProductTag tag : newTags) {
            savedProduct.addTag(tag);
            if (!oldTags.contains(tag)) {
                tag.addProduct(savedProduct);
                saveTag(tag);
            }
        }

        save(savedProduct);
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

    public Collection<ProductSimpleDTO> getAllProductsDTO() {
        return productMapper.toDTOs(productRepository.findAll());
    }

    Collection<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByProductType(ProductType type) {
        return productRepository.findByProductType(type);
    }

    public Collection<ProductSimpleDTO> findByNameContaining(String name) {
        return productMapper.toDTOs(productRepository.findByNameContaining(name));
    }

    public ProductDTO getProduct(long id) {
        return productMapper.toDTO(productRepository.findById(id).orElseThrow());
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
        User seller = product.getSeller();
        if (seller != null) {
            seller.removeProductFromSale(product);
            userRepository.save(seller);
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
            User owner = review.getOwner();
            if (owner != null) {
                owner.removeReview(review);
                userService.saveUser(owner);
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

    public void subFromStock(Long id, int i) {
        Product product = productRepository.findById(id).get();
        product.setStock(product.getStock() - i);
        productRepository.save(product);
    }

    public boolean isOwnerProduct (UserDTO userDTO, ProductDTO productDTO) {
        try {
            Product product = productMapper.toDomain(productDTO); 
            User user = userMapper.toDomain(userDTO);
            return user.getId() == product.getSeller().getId();
        } catch (Exception e) {
            return false;
        }
    }

    // public List<ProductSimpleDTO> isCheckoutCartValid(List<ProductSimpleDTO> products) {

    //     // Check if the cart is empty
    //     if (products.isEmpty()) {
    //         return new ArrayList<>();
    //     }

    //     // Check if all products are in stock
    //     List<ProductSimpleDTO> productsOutOfStock = new ArrayList<>();
    //     for (ProductSimpleDTO product : products) {
    //         if (product.stock() <= 0) {
    //             productsOutOfStock.add(product);
    //         }
    //     }
    //     if (!productsOutOfStock.isEmpty()) {
    //         return productsOutOfStock;
    //     }

    //     // Update the stock of the products in the cart
    //     for (ProductSimpleDTO product : products) {
    //         subFromStock(product.id(), 1);
    //     }

    //     // Create and save the order
    //     Order order = new Order();

    //     // order.setUser();

    //     //     null, null, userMapper.toSimpleDTO(user), user.cartProducts(), new ArrayList<>(),  user.getCartTotalPrice(), PaymentMethod.fromString(paymentMethod), address, phoneNumber);
    //     // order = orderService.save(order);
    //     // model.addAttribute("order", order);
        
    //     // // Clear the cart after confirming the order
    //     // userService.clearCart(user);
    //     // userService.addOrderToUser(user, order);
    // }

    public ProductDTO saveProduct(ProductDTO productDTO, List<MultipartFile> imagesVal, String tagsVal, UserDTO userDTO) {
        Product product = productMapper.toDomain(productDTO);
        User user = userMapper.toDomain(userDTO);
        User userFromBD = userRepository.findById(user.getId()).get();
        product.setSeller(userFromBD);
        product.setTags(transformStringToTags(tagsVal)); 
    
        try {
            if (imagesVal != null && imagesVal.size() > 1) {
                imageService.saveImagesInProduct(product, imagesVal);
            }
        } catch (IOException e) {
            throw new NoSuchElementException("Error loading images"); 
        }

        List<ProductTag> tags = product.getTags();
        for (ProductTag tag : tags) {
            tag.addProduct(product);
        }

        Product savedProduct = save(product);
        userFromBD.addProductForSale(savedProduct);
        userService.saveUser(userFromBD);
        return productMapper.toDTO(savedProduct);
        
    }

    
}