package es.dlj.onlinestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.model.OrderInfo;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.OrderRepository;
import jakarta.transaction.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    public OrderInfo getReferenceById(Long id) {
        return orderRepository.getReferenceById(id);
    }

    public OrderInfo save(OrderInfo order) {
        return orderRepository.save(order);
    }

    @Transactional
    public void delete(Long id) {
        OrderInfo order = orderRepository.findById(id).orElse(null);
        if (order == null) return;
        deepDeleteUser(order);
        deepDeleteProducts(order);
        orderRepository.delete(order);
    }

    @Transactional
    private void deepDeleteUser(OrderInfo order) {
        UserInfo user = order.getUser();
        if (user != null) {
            user.removeOrder(order);
            userService.save(user);
        }
    }

    @Transactional
    private void deepDeleteProducts(OrderInfo order) {
        order.getProducts().clear();
    }
}
