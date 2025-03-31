package es.dlj.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.domain.Review;


public interface ReviewRepository extends JpaRepository<Review, Long> {
}
