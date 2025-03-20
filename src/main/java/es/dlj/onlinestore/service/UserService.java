package es.dlj.onlinestore.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.domain.Review;
import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Checks if there are any users in the database
        // if (userRepository.count() > 0) return;
        userRepository.save(new User("admin", passwordEncoder.encode("password"), "NameAdmin", "SurnameAdmin", "admin@gmail.com", List.of("ADMIN"), "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
        userRepository.save(new User("user", passwordEncoder.encode("password"), "NameUser", "SurnameUser", "user@gmail.com", List.of("USER"), "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
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

}
