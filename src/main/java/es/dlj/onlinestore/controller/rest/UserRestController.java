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

import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.OrderSimpleDTO;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ProductSimpleDTO;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.OrderService;
import es.dlj.onlinestore.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/profile")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired 
    OrderService orderService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById (@PathVariable Long id){
        UserDTO user = userService.findDTOById(id);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/")
    public ResponseEntity<Collection<UserDTO>> getAllUsers(){
        Collection<UserDTO> users = userService.findAllDTOsBy();
        if (users == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(users);    
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser (@RequestBody UserDTO userDTO, UriComponentsBuilder uriBuilder){
        userService.saveDTO(userDTO);
        UserDTO user = userService.findDTOById(userDTO.id());
        if (user == null) return ResponseEntity.notFound().build();
        URI location = uriBuilder.path("/api/profile/{id}").buildAndExpand(user.id()).toUri();
        return ResponseEntity.created(location).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser (@PathVariable Long id, @RequestBody UserDTO newUserDTO) {
        if (userService.findDTOById(id) == null) return ResponseEntity.notFound().build();
        UserDTO user = userService.update(id, newUserDTO);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable Long id) {
        if (userService.findDTOById(id) == null) return ResponseEntity.notFound().build();
        userService.deleteDTOById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/image")
    public ResponseEntity<Object> getUserImage(){
        UserDTO user = userService.getLoggedUserDTO();
        if (user == null) return imageService.loadDefaultImage();
        ImageDTO image = user.profilePhoto();
        if (image == null) return imageService.loadDefaultImage();
        return imageService.loadImage(image.id());
    }

    @GetMapping("/cart")
    public Collection<ProductSimpleDTO> showCart(){
        return userService.getLoggedUserDTO().cartProducts();
    }

    @DeleteMapping("/cart/product/{id}")
    public void removeProduct(@PathVariable Long id){
        userService.removeProductFromCart(id);
        //TODO: return statement;
    }

    @DeleteMapping("/cart/all")
    public void clearCart(){
        userService.clearCart();
        //TODO: return statement;
    }

    @PostMapping("/cart/order")
    public ResponseEntity<OrderSimpleDTO> createOrder(@RequestBody String paymentMethod, String address, String phoneNumber, UriComponentsBuilder  uriBuilder){
        OrderSimpleDTO order = orderService.proceedCheckout(paymentMethod, address, phoneNumber);
        if (order == null) return ResponseEntity.noContent().build();
        URI location =  uriBuilder.path("/api/profile/order/{id}").buildAndExpand(order.id()).toUri();
        return ResponseEntity.created(location).body(order);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<Collection<ProductSimpleDTO>> getProducts(@PathVariable Long id) {
        Collection<ProductSimpleDTO> products = userService.findDTOById(id).cartProducts();
        if (products == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}/products/{productId}")
    public ResponseEntity<ProductSimpleDTO> getProduct(@PathVariable Long id, @PathVariable Long productId) {
        ProductSimpleDTO product = userService.findDTOById(id).cartProducts().stream()
                .filter(p -> p.id().equals(productId))
                .findFirst()
                .orElse(null);
        if (product == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(product);
    }

    @PostMapping("/{id}/products/")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        userService.addProductToCart(productDTO.id());
        return ResponseEntity.status(201).body(productDTO);
    }

    @DeleteMapping("/{id}/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @PathVariable Long productId) {
        userService.removeProductFromCart(productId);
        return ResponseEntity.noContent().build();
    }

    /* 
    @PutMapping("/{id}/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @PathVariable Long productId, @RequestBody ProductDTO productDTO) { 
    }*/

    

    
}
