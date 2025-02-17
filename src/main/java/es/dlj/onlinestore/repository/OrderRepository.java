package es.dlj.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.model.OrderInfo;

public interface OrderRepository extends JpaRepository<OrderInfo, Long> {

}
