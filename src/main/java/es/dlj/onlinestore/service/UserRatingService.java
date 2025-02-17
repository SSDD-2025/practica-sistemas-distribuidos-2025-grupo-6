package es.dlj.onlinestore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.model.UserRating;
import es.dlj.onlinestore.repository.UserRatingRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class UserRatingService {

    @Autowired
    private UserRatingRepository userRatings;

    @Autowired
    private UserService userService;

    @PostConstruct
    @Transactional
    public void init() {
        UserInfo user = userService.findById(1L);  

        UserRating userRating = new UserRating("Good product", "I like it", 4, user);
        userRatings.save(userRating);
        user.addReview(userRating);

        UserRating userRating2 = new UserRating("Bad product", "I don't like it", 1, user);
        userRatings.save(userRating2);
        user.addReview(userRating2);

    }

    public List<UserRating> getUserRatings(UserInfo owner) {
        return userRatings.findByOwner(owner);
    }

}
