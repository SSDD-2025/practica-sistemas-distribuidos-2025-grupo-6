package es.dlj.onlinestore.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserComponent;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private ProductService productService;
    
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
        Optional<Product> product = productService.findById(productId);

        // Remove the product from the cart if it exists
        if (product.isPresent()) {
            userComponent.removeProductFromCart(product.get());
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove/all")
    public String removeProduct() {
        // Remove all the products from the cart
        userComponent.clearCart();
        return "redirect:/cart";
    }
    
    @GetMapping("/checkout")
    public String orderCheckout(Model model) {
        UserInfo user = userComponent.getUser();
        // Check if the cart is empty
        if (user.getCartProducts().isEmpty()) {
            return "redirect:/cart";
        }

        // Check if all products are in stock
        List<Product> productsOutOfStock = new ArrayList<>();
        for (Product product : user.getCartProducts()) {
            if (!product.isInStock()) {
                productsOutOfStock.add(product);
            }
        }
        if (!productsOutOfStock.isEmpty()) {
            model.addAttribute("outOfStockProducts", productsOutOfStock);
            model.addAttribute("user", user);
            return "cart_template";
        }

        // Add the user to the model in case it changes
        model.addAttribute("user", user);
        return "order_checkout_template";
    }
    
    @PostMapping("/confirm-order")
    @Transactional
    public String orderConfirmed(Model model, @RequestParam String paymentMethod, @RequestParam String address, @RequestParam String phoneNumber) {
        // Add the user to the model in case it changes
        UserInfo user = userComponent.getUser();
        model.addAttribute("user", user);

        // Check if the cart is empty
        if (user.getCartProducts().isEmpty()) {
            return "redirect:/cart";
        }

        // Check if all products are in stock
        List<Product> productsOutOfStock = new ArrayList<>();
        for (Product product : user.getCartProducts()) {
            if (!product.isInStock()) {
                productsOutOfStock.add(product);
            }
        }
        if (!productsOutOfStock.isEmpty()) {
            model.addAttribute("outOfStockProducts", productsOutOfStock);
            model.addAttribute("user", user);
            return "cart_template";
        }

        // Update the stock of the products in the cart
        for (Product product : user.getCartProducts()) {
            product.sellOneUnit();
        }

        // Create and save the order
        OrderInfo order = new OrderInfo(user.getCartTotalPrice(), PaymentMethod.fromString(paymentMethod), address, phoneNumber);
        order.setUser(user);
        order.setProducts(new HashSet<>(user.getCartProducts()));
        orderRepository.save(order);
        model.addAttribute("order", order);
        
        // Clear the cart after confirming the order
        user.clearCartProducts();
        user.addOrder(order);

        return "order_confirmed_template";
    }
}
