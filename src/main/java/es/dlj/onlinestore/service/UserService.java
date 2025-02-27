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
        userRepository.deleteAll();
        userRepository.flush();
        userRepository.save(new UserInfo("admin", "admin", "adminName", "adminLastName", "admin@gmail.com", UserInfo.Role.ADMIN, "Calle Tulipan, 1", "Mostoles", "28931", "+34 666 666 666"));
    }

    public UserInfo findByUserNameAndPassword(String userName, String password) {
        return userRepository.findByUserNameAndPassword(userName, password);
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