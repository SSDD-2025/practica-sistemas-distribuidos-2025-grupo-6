package es.dlj.onlinestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.model.Review;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.UserInfoRepository;
import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserInfoRepository userRepository;
    
    @PostConstruct
    public void init() {
        // Checks if there are any users in the database
        // if (userRepository.count() > 0) return;
        userRepository.save(new UserInfo("admin", "Password", "Name", "Surname", "admin@gmail.com", null, "Calle Tulipan, 1", "Mostoles", "28931", "+34123456789"));
    }

    public void save(UserInfo user) {
        userRepository.save(user);
    }

    public UserInfo findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void addReviewToUser(UserInfo user, Review review) {
        user.addReview(review);
        userRepository.save(user);
    }
}