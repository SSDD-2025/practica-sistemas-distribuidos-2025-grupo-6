package es.dlj.onlinestore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import es.dlj.onlinestore.domain.Review;

import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable;


public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByProductId(Long id); 
    Page<Review> findAllByOwnerId(Long id, Pageable pageable);
    Page<Review> findAllByProductId(Long id, Pageable pageable);

}
