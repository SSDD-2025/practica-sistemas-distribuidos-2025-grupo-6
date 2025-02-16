package es.dlj.onlinestore.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.dlj.onlinestore.model.ProductTag;

public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {

    boolean existsByName(String name);
    ProductTag findByName(String name);

    @Query("SELECT new map(t.name as name, COUNT(p) as count) " +
       "FROM ProductTag t LEFT JOIN t.products p " +
       "GROUP BY t " +
       "ORDER BY COUNT(p) DESC, t.name ASC")
    List<Map<String, Object>> findAllWithProductCount();
    
}
