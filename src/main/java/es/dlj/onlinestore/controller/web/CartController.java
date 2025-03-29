package es.dlj.onlinestore.controller.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.dto.OrderDTO;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ProductSimpleDTO;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.dto.UserSimpleDTO;
import es.dlj.onlinestore.enumeration.PaymentMethod;
import es.dlj.onlinestore.mapper.UserMapper;
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

    @Autowired
    private UserMapper userMapper;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            UserSimpleDTO user = userService.findByUserSimpleDTOName(principal.getName());

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
    public String removeProduct(@PathVariable Long productId) {
        UserDTO user = userService.getLoggedUser();

        ProductDTO product = productService.findById(productId);

        userService.removeProductFromCart(user, product);

        return "redirect:/cart";
    }

    @PostMapping("/remove/all")
    public String removeProduct() {
        UserDTO user = userService.getLoggedUser();
        // Remove all the products from the cart
        userService.clearCart(user);
        return "redirect:/cart";
    }
    
    @GetMapping("/checkout")
    public String orderCheckout(Model model) {
        UserDTO user = userService.getLoggedUser();

        // Check if the cart is empty
        if (user.cartProducts().isEmpty()) {
            return "redirect:/cart";
        }

        // Check if all products are in stock
        List<ProductSimpleDTO> productsOutOfStock = new ArrayList<>();
        for (ProductSimpleDTO product : user.cartProducts()) {
            if (product.stock() <= 0) {
                productsOutOfStock.add(product);
            }
        }
        if (!productsOutOfStock.isEmpty()) {
            model.addAttribute("outOfStockProducts", productsOutOfStock);
            return "cart_template";
        }

        return "order_checkout_template";
    }
    
    @PostMapping("/confirm-order")
    @Transactional
    public String orderConfirmed(Model model, @RequestParam String paymentMethod, @RequestParam String address, @RequestParam String phoneNumber) {
        UserDTO user = userService.getLoggedUser();

        // Check if the cart is empty
        if (user.cartProducts().isEmpty()) {
            return "redirect:/cart";
        }

        // Check if all products are in stock
        List<ProductSimpleDTO> productsOutOfStock = new ArrayList<>();
        for (ProductSimpleDTO product : user.cartProducts()) {
            if (product.stock() <= 0) {
                productsOutOfStock.add(product);
            }
        }
        if (!productsOutOfStock.isEmpty()) {
            model.addAttribute("outOfStockProducts", productsOutOfStock);
            return "cart_template";
        }

        // Update the stock of the products in the cart
        for (ProductSimpleDTO product : user.cartProducts()) { 
            productService.subFromStock(product.id(), product.stock() - 1);
        }

        // Create and save the order
        OrderDTO order = new OrderDTO(null, null, userMapper.toSimpleDTO(user), new HashSet<>(user.cartProducts()), new ArrayList<>(),  user.getCartTotalPrice(), PaymentMethod.fromString(paymentMethod), address, phoneNumber);
        order = orderService.save(order);
        model.addAttribute("order", order);
        
        // Clear the cart after confirming the order
        userService.clearCart(user);
        userService.addOrderToUser(user, order);

        return "order_confirmed_template";
    }
}
