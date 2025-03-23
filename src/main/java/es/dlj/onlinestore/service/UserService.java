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
        List<Product> cartProducts = new ArrayList<>(user.getCartProducts());
        for (Product product : cartProducts) {
            user.removeProductFromCart(product);
            productRepository.save(product);
        }
        List<Product> productsForSell = new ArrayList<>(user.getProductsForSell());
        for (Product product : productsForSell) {
            user.removeProductFromSale(product);
            productRepository.save(product);
        }
    }

    @Transactional
    private void deepDeleteOrders(User user) {
        List<Order> orders = new ArrayList<>(user.getOrders());
        for (Order order : orders) {
            List<Product> products = new ArrayList<>(order.getProducts());
            for (Product product : products) {
                order.removeProduct(product);
                productRepository.save(product);
            }
            user.removeOrder(order);  
        }
    }

    @Transactional 
    private void deepDeleteRoles(User user) {
        user.getRoles().clear();
    }


}
