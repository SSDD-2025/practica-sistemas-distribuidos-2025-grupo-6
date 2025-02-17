package es.dlj.onlinestore.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        UserInfo user = userComponent.getUser();
        Set<Product> products = user.getCartProducts();
        model.addAttribute("products", products);
        
        // Calculate total price applying sales
        List<Map<String, Object>> bill = new ArrayList<>();
        Float totalPrice = 0f;
        for (Product product : products) {
            bill.add(Map.of("name", product.getName(), 
                            "price", product.getPrice(), 
                            "sale", product.getPrice() * product.getSale(), 
                            "priceWithSale", product.getPrice() * (1 - product.getSale())));
            totalPrice += product.getPrice() * (1 - product.getSale());
        }

        model.addAttribute("bill", bill);

        model.addAttribute("totalPrice", totalPrice);
        
        return "cart_template";
    }
    
    @PostMapping("/remove/{productId}")
    public String removeProduct(@PathVariable Long productId) {
        UserInfo user = userComponent.getUser();
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            user.removeProductFromCart(product);
        }
        return "redirect:/cart";
    }
    
    @GetMapping("/checkout")
    public String checkout(Model model) {
        UserInfo user = userComponent.getUser();
        model.addAttribute("user", user);
        List<Map<String, Object>> paymentMethods = new ArrayList<>();
        for (PaymentMethod pMethod : PaymentMethod.values()) {
            paymentMethods.add(Map.of("name", pMethod.getName(), 
                                      "selected", (user.getPaymentMethod() != null && user.getPaymentMethod().equals(pMethod))));
        }
        model.addAttribute("paymentMethods", paymentMethods);
        model.addAttribute("address", user.getAddress());
        model.addAttribute("phoneNumber", user.getPhone());
        return "checkout_template";
    }
    
    @PostMapping("/confirm-order")
    public String confirmOrder(@RequestParam String paymentMethod,
                               @RequestParam String address,
                               @RequestParam String phoneNumber,
                               Model model) {

        UserInfo user = userComponent.getUser();
        
        OrderInfo order = new OrderInfo();
        order.setUser(user);
        order.setProducts(user.getCartProducts());
        
        // Calculate total price with sales
        Float totalPrice = 0f;
        for (Product product : user.getCartProducts()) {
            totalPrice += product.getPrice() * (1 - product.getSale());
        }
        
        order.setTotalPrice(totalPrice);
        order.setPaymentMethod(PaymentMethod.fromString(paymentMethod));
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        
        orderRepository.save(order);
        
        // Clear the cart after confirming the order
        user.getCartProducts().clear();
        user.addOrder(order);
        
        model.addAttribute("userName", user.getName());
        model.addAttribute("orderId", order.getId());
        model.addAttribute("totalPrice", totalPrice);

        return "confirmOrder_template";
    }
}
