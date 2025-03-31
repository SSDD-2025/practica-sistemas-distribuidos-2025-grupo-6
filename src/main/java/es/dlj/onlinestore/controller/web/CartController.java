package es.dlj.onlinestore.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.dto.OrderSimpleDTO;
import es.dlj.onlinestore.service.OrderService;
import es.dlj.onlinestore.service.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showCart(Model model) {
        return "cart_template";
    }
    
    @PostMapping("/remove/{productId}")
    public String removeProduct(@PathVariable Long productId) {
        userService.removeProductFromCart(productId);
        return "redirect:/cart";
    }

    @PostMapping("/remove/all")
    public String removeProduct() {
        userService.clearCart();
        return "redirect:/cart";
    }
    
    @GetMapping("/checkout")
    public String orderCheckout(Model model) {
        if (!orderService.isCheckoutCartValid()) {
            model.addAttribute("error", "Your cart is empty or contains products that are not in stock.");
            return "redirect:/cart";
        } else {
            return "order_checkout_template";
        }
    }
    
    @PostMapping("/confirm-order")
    public String orderConfirmed(Model model, @RequestParam String paymentMethod, @RequestParam String address, @RequestParam String phoneNumber) {
        OrderSimpleDTO order = orderService.proceedCheckout(paymentMethod, address, phoneNumber);
        if (order == null) {
            model.addAttribute("error", "Your cart is empty or contains products that are not in stock.");
            return "redirect:/cart";
        }
        model.addAttribute("order", order);
        return "order_confirmed_template";
    }
}
