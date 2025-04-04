package es.dlj.onlinestore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.domain.Order;
import es.dlj.onlinestore.domain.Product;

import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable;

public interface OrderRepository extends JpaRepository<Order, Long> {

    public List<Order> findByProductsContaining(Product product);
    public Page<Order> findByUserId(Long id, Pageable pageable);

}
