package es.dlj.onlinestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.domain.Order;
import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.dto.OrderDTO;
import es.dlj.onlinestore.mapper.OrderMapper;
import es.dlj.onlinestore.repository.OrderRepository;
import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper;

    public OrderDTO getReferenceById(Long id) {
        return orderMapper.toDTO(orderRepository.getReferenceById(id));
    }

    public OrderDTO save(OrderDTO order) {
        Order orderEntity = orderMapper.toDomain(order);
        Order savedOrder = orderRepository.save(orderEntity);
        return orderMapper.toDTO(savedOrder);
    }

    @Transactional
    public void delete(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return;
        deepDeleteUser(order);
        deepDeleteProducts(order);
        orderRepository.delete(order);
    }

    @Transactional
    private void deepDeleteUser(Order order) {
        User user = order.getUser();
        if (user != null) {
            user.removeOrder(order);
            userService.save(user);
        }
    }

    @Transactional
    private void deepDeleteProducts(Order order) {
        order.getProducts().clear();
    }
}
