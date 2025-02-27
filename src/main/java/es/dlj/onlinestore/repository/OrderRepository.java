package es.dlj.onlinestore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.model.OrderInfo;
import es.dlj.onlinestore.model.Product;

public interface OrderRepository extends JpaRepository<OrderInfo, Long> {

    public List<OrderInfo> findByProductsContaining(Product product);

}
