package es.dlj.onlinestore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.model.UserRating;
import es.dlj.onlinestore.repository.UserRatingRepository;
import jakarta.annotation.PostConstruct;

@Service
public class UserRatingService {

    @Autowired
    private UserRatingRepository userRatings;

    @PostConstruct
    public void init() {
        // Some user ratings
    }

    public List<UserRating> getUserRatings(UserInfo owner) {
        return userRatings.findByOwner(owner);
    }

}
