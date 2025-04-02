package es.dlj.onlinestore.controller.rest;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


import es.dlj.onlinestore.dto.OrderSimpleDTO;
import es.dlj.onlinestore.dto.ProductSimpleDTO;
import es.dlj.onlinestore.service.OrderService;
import es.dlj.onlinestore.service.UserService;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    private UserService userService;

    @Autowired OrderService orderService;

    @GetMapping
    public Collection<ProductSimpleDTO> showCart(){
        return userService.getLoggedUserDTO().cartProducts();
    }

    @DeleteMapping("/product/{id}")
    public void removeProduct(@PathVariable Long id){
        userService.removeProductFromCart(id);
        //TODO: return statement;
    }

    @DeleteMapping("/all")
    public void clearCart(){
        userService.clearCart();
        //TODO: return statement;
    }

    @PostMapping("/order")
    public ResponseEntity<OrderSimpleDTO> createOrder(@RequestBody String paymentMethod, String address, String phoneNumber, UriComponentsBuilder  uriBuilder){
        OrderSimpleDTO order = orderService.proceedCheckout(paymentMethod, address, phoneNumber);
        if (order == null) return ResponseEntity.noContent().build();
        URI location =  uriBuilder.path("/api/profile/order/{id}").buildAndExpand(order.id()).toUri();
        return ResponseEntity.created(location).body(order);
    }


    
}
