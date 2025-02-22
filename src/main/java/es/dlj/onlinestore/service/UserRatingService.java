package es.dlj.onlinestore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.model.Review;
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

        Review userRating = new Review("Good product", "I like it", 4, user);
        userRatings.save(userRating);
        user.addReview(userRating);

        Review userRating2 = new Review("Bad product", "I don't like it", 1, user);
        userRatings.save(userRating2);
        user.addReview(userRating2);

        Review userRating3 = new Review("Perfect product", "I love it", 5, user);
        userRatings.save(userRating3);
        user.addReview(userRating3);

        Review userRating4 = new Review("Fragile product", "I don't like it, I used it twice and it's already broken", 2, user);
        userRatings.save(userRating4);
        user.addReview(userRating4);

        Review userRating5 = new Review("Very good product", "It is useful", 5, user);
        userRatings.save(userRating5);
        user.addReview(userRating5);

        Review userRating6 = new Review("Bad product", "I hate it", 1, user);
        userRatings.save(userRating6);
        user.addReview(userRating6);

    }

    public List<Review> getUserRatings(UserInfo owner) {
        return userRatings.findByOwner(owner);
    }

    /* 
    public void saveReview(Review review) {
        UserInfo user = userService.findById(1L); 
        userRatings.save(review);
        user.addReview(review);
        
    }*/


}
