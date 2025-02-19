package es.dlj.onlinestore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.model.Review;


public interface UserRatingRepository extends JpaRepository<Review, Long> {

    List<Review> findByOwner(UserInfo owner);

}
