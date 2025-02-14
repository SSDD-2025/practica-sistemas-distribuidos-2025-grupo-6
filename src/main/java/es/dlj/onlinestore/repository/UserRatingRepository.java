package es.dlj.onlinestore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.model.UserRating;


public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

    List<UserRating> findByOwner(UserInfo owner);

}
