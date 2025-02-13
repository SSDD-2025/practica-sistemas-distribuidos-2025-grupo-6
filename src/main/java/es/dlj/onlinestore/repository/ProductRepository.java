package es.dlj.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.model.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {

   

}
