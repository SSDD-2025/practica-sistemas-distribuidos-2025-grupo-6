package es.dlj.onlinestore.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.domain.Image;
import es.dlj.onlinestore.domain.Order;
import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.domain.Review;
import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.OrderDTO;
import es.dlj.onlinestore.mapper.ImageMapper;
import es.dlj.onlinestore.mapper.OrderMapper;
import es.dlj.onlinestore.dto.ReviewDTO;
import es.dlj.onlinestore.mapper.ProductMapper;
import es.dlj.onlinestore.mapper.ReviewMapper;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.dto.UserFormDTO;
import es.dlj.onlinestore.dto.UserSimpleDTO;
import es.dlj.onlinestore.mapper.UserMapper;
import es.dlj.onlinestore.repository.ImageRepository;
import es.dlj.onlinestore.repository.OrderRepository;
import es.dlj.onlinestore.repository.ProductRepository;
import es.dlj.onlinestore.repository.ReviewRepository;
import es.dlj.onlinestore.repository.UserRepository;
import jakarta.annotation.PostConstruct;
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
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private ImageMapper imageMapper;

    @PostConstruct
    public void init() {
        userRepository.save(new User("admin", passwordEncoder.encode("password"), "NameAdmin", "SurnameAdmin", "admin@gmail.com", 
            List.of("ADMIN"), "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
        userRepository.save(new User("user", passwordEncoder.encode("password"), "NameUser", "SurnameUser", "user@gmail.com", 
            List.of("USER"), "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
    }

    public void setUserProfilePhoto(ImageDTO imageDTO) {
        User user = getLoggedUser();
        Image image = imageMapper.toDomain(imageDTO);
        user.setProfilePhoto(image);
        userRepository.save(user);
    }

    public void removeProductFromCart(Long productId) {
        User user = getLoggedUser();
        Product product = productRepository.findById(productId).orElseThrow();
        user.removeProductFromCart(product);
        userRepository.save(user);
    }

    public void clearCart() {
        User user = getLoggedUser();
        user.clearCart();
        userRepository.save(user);
    }

    void addOrderToUser(Order order) {
        System.out.println("Adding order to user: " + order);
        User user = getLoggedUser();
        user.addOrder(order);
        userRepository.save(user);
    }
    
    public List<String> checkNewUser(UserFormDTO newUser) {
        if (!newUser.password().equals(newUser.repeatedPassword())) {
            return List.of("confirmPasswordError", "Passwords do not match");
        }
        if (existsUserByUsername(newUser.username())) {
            return List.of("usernameError", "User already exists, try to login instead");
        }
        return null;
    }

    public void saveDTO(UserDTO userDTO) {
        User user = userMapper.toDomain(userDTO);
        save(user);
    }

    public void saveDTO(UserFormDTO userFromDTO) {
        User user = userMapper.toDomain(userFromDTO);
        save(user);
    }

    User save(User user) {
        return userRepository.save(user);
    }

    // TODO: Fix
    public UserDTO replaceUser (Long id, UserDTO userDTO) {
        if (userRepository.existsById(id)) {
            User updateUser = userMapper.toDomain(userDTO);
            updateUser.setId(id);
            userRepository.save(updateUser);
            return userMapper.toDTO(updateUser);
        } else {
            throw new NoSuchElementException(); 
        }
    }

    public Collection<UserDTO> getUsers() {
        return userMapper.toDTOs(userRepository.findAll());
    } 

    public UserDTO findUserDTOById(Long id) {
        return userMapper.toDTO(userRepository.findById(id).orElse(null));
    }

    User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public boolean existsUserByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserDTO getLoggedUserDTO() {
        return userMapper.toDTO(getLoggedUser());
    }

    User getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            return userRepository.findByUsername(auth.getName()).get();
        }
        return null;
    }

    @Transactional
    public UserDTO deleteUserDTO(Long id) {
        User user = deleteUser(id);
        return userMapper.toDTO(user);
    }

    @Transactional
    public User deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        deepDeleteProducts(user);
        deepDeleteReviews(user);
        deepDeleteOrders(user);
        deepDeleteImage(user);
        deepDeleteRoles(user);
        userRepository.delete(user);
        return user;
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
            imageRepository.delete(image);
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

    public void addProductToCart(Long ProductId) {
        User user = getLoggedUser();
        Product product = productRepository.findById(ProductId).orElseThrow();
        user.addProductToCart(product);
        userRepository.save(user);
    }

    public UserSimpleDTO findByUserSimpleDTOName(String name) {
        return userMapper.toSimpleDTO(userRepository.findByUsername(name).orElseThrow());
    }

}
