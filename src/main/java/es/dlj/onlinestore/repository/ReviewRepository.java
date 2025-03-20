package es.dlj.onlinestore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.domain.Review;
import es.dlj.onlinestore.domain.User;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByOwner(User owner);
    
    List<Review> findByProduct(Product product);
    
}
