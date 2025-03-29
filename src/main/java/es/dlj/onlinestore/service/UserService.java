package es.dlj.onlinestore.service;

import java.security.Principal;
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
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ReviewDTO;
import es.dlj.onlinestore.mapper.ProductMapper;
import es.dlj.onlinestore.mapper.ReviewMapper;
import es.dlj.onlinestore.dto.UserDTO;
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
    private ProductMapper productMapper;

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

    public void setProfilePhoto(UserDTO userDTO, ImageDTO imageDTO) {
        User user = userMapper.toDomain(userDTO);
        Image image = imageMapper.toDomain(imageDTO);
        user.setProfilePhoto(image);
        userRepository.save(user);
    }

    public void removeProductFromCart(UserDTO userDTO, ProductDTO product) {
        User user = userMapper.toDomain(userDTO);
        user.removeProductFromCart(productMapper.toDomain(product));
        userRepository.save(user);
    }

    public UserDTO clearCart(UserDTO userDTO) {
        User user = userMapper.toDomain(userDTO);
        user.clearCart();
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    public void addOrderToUser(UserDTO user, OrderDTO order) {
        User userDomain = userMapper.toDomain(user);
        Order orderDomain = orderMapper.toDomain(order);
        userDomain.addOrder(orderDomain);
        userRepository.save(userDomain);
    }
    
    public UserDTO createUserDTO(UserDTO userDTO) {
        User user = userMapper.toDomain(userDTO);
        if (user.getRoles().isEmpty()) user.setRoles(List.of("USER"));
        String encodedPassword = passwordEncoder.encode(user.getEncodedPassword());
        user.setEncodedPassword(encodedPassword);
        userRepository.save(user); 
        return userMapper.toDTO(user);
    }

    public UserDTO saveUserDTO(UserDTO userDTO) {
        User user = userMapper.toDomain(userDTO);
        saveUser(user);
        return userMapper.toDTO(user);
    }

    User saveUser(User user) {
        return userRepository.save(user);
    }

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
    
    public Optional<UserDTO> findByUserDTOName(String username) {
        return userRepository.findByUsername(username).map(userMapper::toDTO);
    }

    public void addReviewToUser(UserDTO userDTO, ReviewDTO reviewDTO) {
        User user = userMapper.toDomain(userDTO);
        Review review = reviewMapper.toDomain(reviewDTO);
        user.addReview(review);
        userRepository.save(user);
    }

    public UserDTO getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Check if user is authenticated and return the user
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            return findByUserDTOName(auth.getName()).get();
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

    public void addProductToCart(ProductDTO product, UserDTO userDTO) {
        User user = userMapper.toDomain(userDTO);
        Product productDomain = productMapper.toDomain(product);
        user.addProductToCart(productDomain);
        userRepository.save(user);
    }

    public UserSimpleDTO findByUserSimpleDTOName(String name) {
        return userMapper.toSimpleDTO(userRepository.findByUsername(name).orElseThrow());
    }

}
