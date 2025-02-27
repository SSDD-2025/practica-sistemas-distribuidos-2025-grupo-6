package es.dlj.onlinestore.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.enumeration.PaymentMethod;
import es.dlj.onlinestore.model.OrderInfo;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.OrderRepository;
import es.dlj.onlinestore.repository.ProductRepository;
import es.dlj.onlinestore.service.UserComponent;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private UserComponent userComponent;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @GetMapping
    public String showCart(Model model) {
        // Add the user to the model in case it changes
        model.addAttribute("user", userComponent.getUser());
        return "cart_template";
    }
    
    @PostMapping("/remove/{productId}")
    public String removeProduct(@PathVariable Long productId) {
        // Find the product by its ID
        Optional<Product> product = productRepository.findById(productId);

        // Remove the product from the cart if it exists
        if (product.isPresent()) {
            userComponent.getUser().removeProductFromCart(product.get());
        }
        return "redirect:/cart";
    }
    
    @GetMapping("/checkout")
    public String checkout(Model model) {
        // Add the user to the model in case it changes
        model.addAttribute("user", userComponent.getUser());
        return "checkout_template";
    }
    
    @PostMapping("/confirm-order")
    public String confirmOrder(Model model, @RequestParam String paymentMethod, @RequestParam String address, @RequestParam String phoneNumber) {
        // Add the user to the model in case it changes
        UserInfo user = userComponent.getUser();
        model.addAttribute("user", user);

        // Create and save the order
        OrderInfo order = new OrderInfo(user.getCartProducts(), user.getCartTotalPrice(), PaymentMethod.fromString(paymentMethod), address, phoneNumber);
        order.setUser(user);
        orderRepository.save(order);
        model.addAttribute("order", order);
        
        // Clear the cart after confirming the order
        user.clearCartProducts();
        user.addOrder(order);

        return "confirmOrder_template";
    }
}
