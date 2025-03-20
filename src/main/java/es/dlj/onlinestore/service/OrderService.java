package es.dlj.onlinestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.domain.Order;
import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.repository.OrderRepository;
import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    public Order getReferenceById(Long id) {
        return orderRepository.getReferenceById(id);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
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
