package es.dlj.onlinestore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.model.Review;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.UserInfoRepository;
import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserInfoRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Checks if there are any users in the database
        // if (userRepository.count() > 0) return;
        userRepository.save(new UserInfo("admin", passwordEncoder.encode("password"), "NameAdmin", "SurnameAdmin", "admin@gmail.com", List.of("admin"), "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
        userRepository.save(new UserInfo("user", passwordEncoder.encode("password"), "NameUser", "SurnameUser", "user@gmail.com", List.of("user "), "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
    }

    public void save(UserInfo user) {
        userRepository.save(user);
    }

    public UserInfo findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<UserInfo> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public void addReviewToUser(UserInfo user, Review review) {
        user.addReview(review);
        userRepository.save(user);
    }

}
