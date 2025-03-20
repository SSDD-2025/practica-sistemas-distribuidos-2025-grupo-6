package es.dlj.onlinestore.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.domain.Order;
import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.enumeration.PaymentMethod;
import es.dlj.onlinestore.service.OrderService;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            User user = userService.findByUserName(principal.getName()).get();
            model.addAttribute("user", user);
            model.addAttribute("isLogged", true);
            model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
            model.addAttribute("isUser", request.isUserInRole("USER"));

        } else {
            model.addAttribute("isLogged", false);
        }
    }
    
    @GetMapping
    public String showCart(Model model) {
        return "cart_template";
    }
    
    @PostMapping("/remove/{productId}")
    public String removeProduct(@PathVariable Long productId, HttpServletRequest request) {
        User user = userService.getLoggedUser(request);
        if (user == null) return "redirect:/login";

        // Find the product by its ID
        Optional<Product> product = productService.findById(productId);

        // Remove the product from the cart if it exists
        if (product.isPresent()) {
            user.removeProductFromCart(product.get());
        }
        userService.save(user);
        return "redirect:/cart";
    }

    @PostMapping("/remove/all")
    public String removeProduct(HttpServletRequest request) {
        User user = userService.getLoggedUser(request);
        if (user == null) return "redirect:/login";

        // Remove all the products from the cart
        user.clearCart();
        userService.save(user);
        return "redirect:/cart";
    }
    
    @GetMapping("/checkout")
    public String orderCheckout(Model model, HttpServletRequest request) {
        User user = userService.getLoggedUser(request);
        if (user == null) return "redirect:/login";

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

        return "order_checkout_template";
    }
    
    @PostMapping("/confirm-order")
    @Transactional
    public String orderConfirmed(Model model, @RequestParam String paymentMethod, @RequestParam String address, @RequestParam String phoneNumber, HttpServletRequest request) {
        User user = userService.getLoggedUser(request);
        if (user == null) return "redirect:/login";

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
            // model.addAttribute("user", user);
            return "cart_template";
        }

        // Update the stock of the products in the cart
        for (Product product : user.getCartProducts()) {
            product.sellOneUnit();
        }

        // Create and save the order
        Order order = new Order(user.getCartTotalPrice(), PaymentMethod.fromString(paymentMethod), address, phoneNumber);
        order.setUser(user);
        order.setProducts(new HashSet<>(user.getCartProducts()));
        orderService.save(order);
        model.addAttribute("order", order);
        
        // Clear the cart after confirming the order
        user.clearCartProducts();
        user.addOrder(order);
        userService.save(user);

        return "order_confirmed_template";
    }
}
