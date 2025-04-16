package es.dlj.onlinestore.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import es.dlj.onlinestore.dto.OrderDTO;
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


//TODO: init 2 usuarios registrados y meter la contraseña cifrada por BCrypt del admin en el properties.
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;


    @PostConstruct
    public void init() {
        userRepository.save(new User("admin", passwordEncoder.encode("password"), "NameAdmin", "SurnameAdmin", "admin@gmail.com", 
            List.of("ADMIN","USER"), "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
        userRepository.save(new User("user", passwordEncoder.encode("password"), "NameUser", "SurnameUser", "user@gmail.com", 
            List.of("USER"), "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
        userRepository.save(new User("user2", passwordEncoder.encode("password"), "NameUser2", "SurnameUser2", "user2@gmail.com", 
            List.of("USER"), "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
    
        
    }

    public Collection<UserSimpleDTO> getAllUsers(){
        if (!getLoggedUser().getRoles().contains("ADMIN")) return new ArrayList<>();
        return userMapper.toSimpleDTOs(userRepository.findAll());
    }

    public void removeProductFromCart(Long productId) {
        User user = getLoggedUser();
        user.removeProductFromCartById(productId);
        userRepository.save(user);
    }

    public void clearCart() {
        User user = getLoggedUser();
        user.clearCart();
        userRepository.save(user);
    }

    void addOrderToUser(Order order) {
        User user = getLoggedUser();
        user.addOrder(order);
        userRepository.save(user);
    }
    
    public List<String> checkNewUserError(UserFormDTO newUser) {
        if (!newUser.password().equals(newUser.repeatedPassword())) {
            return List.of("confirmPasswordError", "Passwords do not match");
        }
        if (existsUserByUsername(newUser.username())) {
            return List.of("usernameError", "User already exists, try to login instead");
        }
        return null;
    }

    public UserDTO saveDTO(UserDTO userDTO) {
        return userMapper.toDTO(save(userMapper.toDomain(userDTO)));
    }

    public UserDTO saveDTO(UserFormDTO userFromDTO) {
        return userMapper.toDTO(save(userMapper.toDomain(userFromDTO)));
        
    }

    User save(User user) {
        return userRepository.save(user);
    }

    public UserDTO update (Long id, UserDTO userDTO) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).orElseThrow();
            if (userDTO.username() != null) user.setUsername(userDTO.username());
            if (userDTO.name() != null) user.setName(userDTO.name());
            if (userDTO.surname() != null) user.setSurname(userDTO.surname());
            if (userDTO.email() != null) user.setEmail(userDTO.email());
            if (userDTO.address() != null) user.setAddress(userDTO.address());
            if (userDTO.city() != null) user.setCity(userDTO.city());
            if (userDTO.postalCode() != null) user.setPostalCode(userDTO.postalCode());
            if (userDTO.phone() != null) user.setPhone(userDTO.phone());
            if (userDTO.paymentMethod() != null) user.setPaymentMethod(userDTO.paymentMethod().toString());
            user = userRepository.save(user);
            return userMapper.toDTO(user);
        } else {
            throw new NoSuchElementException(); 
        }
    } 

    public UserSimpleDTO findSimpleDTOByName(String name) {
        return userMapper.toSimpleDTO(userRepository.findByUsername(name).orElseThrow());
    }

    User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public UserDTO findDTOById(Long id) {
        return userMapper.toDTO(findById(id));
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

    public UserDTO addProductToCart(Long ProductId) {
        User user = getLoggedUser();
        Product product = productRepository.findById(ProductId).orElseThrow();
        user.addProductToCart(product);
        user = userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Transactional
    public UserDTO deleteDTOById(Long id) {
        return userMapper.toDTO(deleteById(id));
    }

    @Transactional
    private User deleteById(Long id) {
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

    public Collection<UserDTO> findAllDTOsBy() {
        Collection<UserDTO> users = new ArrayList<>();
        for (User u : userRepository.findAll()) {
            users.add(userMapper.toDTO(u));
        }
        return users;
    }
}