package es.dlj.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.domain.ProductTag;

public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {

    boolean existsByName(String name);
    ProductTag findByName(String name);
    
}
