package es.dlj.onlinestore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.Review;
import es.dlj.onlinestore.model.UserInfo;


public interface UserReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByOwner(UserInfo owner);
    
    List<Review> findByProduct(Product product);
    
}
