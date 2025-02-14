package es.dlj.onlinestore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.model.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceBetween(float minPrice, float maxPrice);

}
