package es.dlj.onlinestore.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import es.dlj.onlinestore.domain.Order;
import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.dto.OrderDTO;
import es.dlj.onlinestore.dto.OrderSimpleDTO;
import es.dlj.onlinestore.enumeration.PaymentMethod;
import es.dlj.onlinestore.mapper.OrderMapper;
import es.dlj.onlinestore.repository.OrderRepository;
import jakarta.transaction.Transactional;


@Service
public class OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper;

    public OrderDTO findDTOById(Long id) {
        return orderMapper.toDTO(orderRepository.getReferenceById(id));
    }

    public boolean isCheckoutCartValid() {
        User user = userService.getLoggedUser();
        Set<Product> cartProducts = user.getCartProducts();
        // Check if the cart is empty
        if (cartProducts.isEmpty()) return false;
        // Check if all products are in stock
        for (Product product : cartProducts) {
            if (product.getStock() <= 0) return false;
        }
        return true;
    }

    @Transactional
    public OrderSimpleDTO proceedCheckout(String paymentMethod, String address, String phoneNumber) {

        if (!isCheckoutCartValid()) return null;

        User user = userService.getLoggedUser();
        Set<Product> cartProducts = user.getCartProducts();

        // Update the stock of the products in the cart
        for (Product product : cartProducts) { 
            product.sellOneUnit();
            productService.save(product);
        }

        // Create and save the order
        Order order = new Order();
        order.setUser(user);
        order.setProducts(new HashSet<>(cartProducts));
        order.setTotalPrice(user.getCartTotalPrice());
        order.setPaymentMethod(PaymentMethod.fromString(paymentMethod));
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order = save(order);

        userService.clearCart();
        userService.addOrderToUser(order);

        return orderMapper.toSimpleDTO(order);
    }

    Order save(Order order) {
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

    public Page<OrderSimpleDTO> getOrdersByUserId(Long id, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUserId(id, pageable);
        return orders.map(orderMapper::toSimpleDTO);
    }

    public Page<OrderDTO> getAllOrdersByUserId(Long id, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUserId(id, pageable);
        return orders.map(orderMapper::toDTO);
    }
}
