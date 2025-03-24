package es.dlj.onlinestore.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.domain.Image;
import es.dlj.onlinestore.domain.Order;
import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.domain.Review;
import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.dto.OrderDTO;
import es.dlj.onlinestore.dto.OrderMapper;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ProductMapper;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.dto.UserMapper;
import es.dlj.onlinestore.repository.OrderRepository;
import es.dlj.onlinestore.repository.ProductRepository;
import es.dlj.onlinestore.repository.ReviewRepository;
import es.dlj.onlinestore.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @PostConstruct
    public void init() {
        // Checks if there are any users in the database
        // if (userRepository.count() > 0) return;
        //userRepository.save(new User("admin", passwordEncoder.encode("password"), "NameAdmin", "SurnameAdmin", "admin@gmail.com", List.of("ADMIN"), "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
        //userRepository.save(new User("user", passwordEncoder.encode("password"), "NameUser", "SurnameUser", "user@gmail.com", List.of("USER"), "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
        if (userRepository.findByUserName("admin").isEmpty()) {
            userRepository.save(new User("admin", passwordEncoder.encode("password"), "NameAdmin", "SurnameAdmin", "admin@gmail.com", 
                List.of("ADMIN"), "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
        }
        
        if (userRepository.findByUserName("user").isEmpty()) {
            userRepository.save(new User("user", passwordEncoder.encode("password"), "NameUser", "SurnameUser", "user@gmail.com", 
                List.of("USER"), "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
        }
    
    }

    public void removeProductFromCart(UserDTO userDTO, ProductDTO product) {
        User user = userMapper.toDomain(userDTO);
        user.removeProductFromCart(productMapper.toDomain(product));
        userRepository.save(user);
    }

    public void clearCart(UserDTO userDTO) {
        User user = userMapper.toDomain(userDTO);
        user.clearCart();
        userRepository.save(user);
    }

    public void addOrderToUser(UserDTO user, OrderDTO order) {
        User userDomain = userMapper.toDomain(user);
        Order orderDomain = orderMapper.toDomain(order);
        userDomain.addOrder(orderDomain);
        userRepository.save(userDomain);
    }

    public User save(User user) {
        if (user.getRoles().isEmpty()) user.setRoles(List.of("USER")); 
        String encodedPassword = passwordEncoder.encode(user.getEncodedPassword());
        user.setEncodedPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public void addReviewToUser(User user, Review review) {
        user.addReview(review);
        userRepository.save(user);
    }

    public User getLoggedUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) return null;
        return findByUserName(principal.getName()).get();
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return;
        deepDeleteProducts(user);
        deepDeleteReviews(user);
        deepDeleteOrders(user);
        deepDeleteImage(user);
        deepDeleteRoles(user);
        userRepository.delete(user);
    }

    @Transactional
    private void deepDeleteReviews(User user) {
        List<Review> reviews = new ArrayList<>(user.getReviews());
        for (Review review : reviews) {
            Product product = review.getProduct();
            if (product != null) {
                product.removeReview(review);
                productRepository.save(product);
            }
            review.setOwner(null);
            user.getReviews().remove(review);
            reviewRepository.delete(review);
        }
    }

    @Transactional
    private void deepDeleteImage(User user) {
        Image image = user.getProfilePhoto();
        if (image != null) {
            imageService.delete(image);
            user.setProfilePhoto(null);            
        }
    }

    @Transactional
    private void deepDeleteProducts(User user) {
        user.getCartProducts().clear();

        List<Product> productsForSell = new ArrayList<>(user.getProductsForSell());
        for (Product product : productsForSell) {
            List<Review> reviews = new ArrayList<>(product.getReviews());
            for (Review review : reviews) {
                reviewRepository.delete(review);
            }
            product.getReviews().clear();
    
            List<Order> ordersWithProduct = orderRepository.findByProductsContaining(product);
            for (Order order : ordersWithProduct) {
                order.getProducts().remove(product);
                orderRepository.save(order);
            }
    
            productRepository.delete(product);
        }
        
        user.getProductsForSell().clear();
    }

    @Transactional
    private void deepDeleteOrders(User user) {
        List<Order> orders = new ArrayList<>(user.getOrders());
        for (Order order : orders) {
            orderRepository.delete(order);
            user.removeOrder(order);  
        }
    }

    @Transactional 
    private void deepDeleteRoles(User user) {
        user.getRoles().clear();
    }

}
