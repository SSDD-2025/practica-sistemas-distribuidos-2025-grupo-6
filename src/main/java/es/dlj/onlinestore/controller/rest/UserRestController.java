package es.dlj.onlinestore.controller.rest;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.OrderDTO;
import es.dlj.onlinestore.dto.OrderFormtDTO;
import es.dlj.onlinestore.dto.OrderSimpleDTO;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ProductSimpleDTO;
import es.dlj.onlinestore.dto.ReviewDTO;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.dto.UserFormDTO;
import es.dlj.onlinestore.dto.UserSimpleDTO;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.OrderService;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.ReviewService;
import es.dlj.onlinestore.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users/")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired 
    OrderService orderService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    private boolean isActionAllowed(Long id){
        UserDTO userDTO = userService.getLoggedUserDTO();
        return userDTO.roles().contains("ADMIN") || userDTO.id() == id;   
    }

    private static final Map<String, String> EXTENSIONS = Map.of(
        ".jpg", "image/jpeg", 
        ".png", "image/png", 
        ".gif", "image/gif"
    );

    @GetMapping("/")
    public ResponseEntity<Collection<UserSimpleDTO>> getUsers(){
        UserDTO userDTO = userService.getLoggedUserDTO();
        if (!userDTO.roles().contains("ADMIN")) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Collection<UserSimpleDTO> users = userService.getAllUsers();
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        if (isActionAllowed(id)) {
            return ResponseEntity.ok(userService.findDTOById(id));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Object> registerUser(
            @Validated @RequestBody UserFormDTO user,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()){
             Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // Check if the password and repeated password match
        List<String> error = userService.checkNewUserError(user);
        if (error != null) return ResponseEntity.badRequest().body(error);

        UserDTO userDTO = userService.saveDTO(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userDTO.id()).toUri();

        return ResponseEntity.created(location).body(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(
            @Valid @RequestBody UserDTO newUserDTO,
            BindingResult bindingResult,
            @PathVariable Long id
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        if (bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
           for (FieldError error : bindingResult.getFieldErrors()) {
               errors.put(error.getField(), error.getDefaultMessage());
           }
           return ResponseEntity.badRequest().body(errors);
       }

        UserDTO userDTO = userService.findDTOById(id);

        userDTO = userService.update(userDTO.id(), newUserDTO);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id
    ){

        if (!isActionAllowed(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try{
            UserDTO userDTO = userService.deleteDTOById(id);
            return ResponseEntity.ok("User" + userDTO.username() + "deleted successfuly.");
        } catch (Error e) {
            return ResponseEntity.badRequest().body("Login first in order to delete the account.");
        }
    }

    @GetMapping("/{id}/products/")
    public ResponseEntity<Page<ProductDTO>> getSellingProducts(
            @PathVariable Long id, 
            @RequestBody(required = false) Integer size,
            @RequestParam(required = false) Integer page
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 4;
        Page<ProductDTO> products = productService.getAllProductsByUserId(id, PageRequest.of(pageNum, pageSize));
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}/reviews/")
    public ResponseEntity<Page<ReviewDTO>> getReviews(
            @PathVariable Long id, 
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Integer page
    ) {
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 4;
        Page<ReviewDTO> reviewsPage = reviewService.findAllReviewsByUserIdPag(id, PageRequest.of(pageNum, pageSize));
        if (reviewsPage.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(reviewsPage);
    }

    @GetMapping("/{id}/image/")
    public ResponseEntity<Object> getProfileImage(
        @PathVariable Long id
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        try{
            UserDTO userDTO = userService.getLoggedUserDTO();
            Resource imageAPI = imageService.loadAPIImage(userDTO.profilePhoto().id());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, EXTENSIONS.get(userDTO.profilePhoto().contentType())).body(imageAPI);
        } catch(NoSuchElementException e){
            return imageService.loadDefaultImage();
        }
    }

    @PostMapping("/{id}/image/")
    public ResponseEntity<ImageDTO> addProfileImage(
            @RequestBody MultipartFile imageFile,
            @PathVariable Long id
        ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        try {
            imageService.saveImageInUser(imageFile);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
            return ResponseEntity.created(location).build();

        } catch (IOException e) {
            
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/image/")
    public ResponseEntity<Object> updateProfileImage (
            @RequestBody MultipartFile imageFile,
            @PathVariable Long id
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        try {
            UserDTO userDTO = imageService.saveImageInUser(imageFile);
            Resource imageAPI = imageService.loadAPIImage(userDTO.profilePhoto().id());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, EXTENSIONS.get(userDTO.profilePhoto().contentType())).body(imageAPI);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        
    }

    @DeleteMapping("/{id}/image/")
    public ResponseEntity<?> deleteProfileImage(
        @PathVariable Long id
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        UserDTO userDTO = userService.findDTOById(id);
        ImageDTO oldPhotoDTO = userDTO.profilePhoto();
        if (oldPhotoDTO != null) {
            userService.deleteProfileImage(userDTO);
            imageService.delete(oldPhotoDTO);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/cart/")
    public ResponseEntity<Collection<ProductSimpleDTO>> getCart(
            @PathVariable Long id
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        UserDTO userDTO = userService.findDTOById(id);
        return ResponseEntity.ok(userDTO.cartProducts());
    }

    @DeleteMapping("/{id}/cart/")
    public ResponseEntity<Object> clearCart(
            @PathVariable Long id
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        userService.clearCart();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cart/products/{productId}")
    public ResponseEntity<Object> addProductToCart(
            @PathVariable Long id,
            @PathVariable Long productId
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        userService.addProductToCart(productId);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{id}/cart").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}/cart/products/{productId}")
    public ResponseEntity<Object> removeProductToCart(
            @PathVariable Long id,
            @PathVariable Long productId
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        userService.removeProductFromCart(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/orders/")
    public ResponseEntity<Page<OrderDTO>> getOrders(
            @PathVariable Long id, 
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Integer page
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 4;
        Page<OrderDTO> orders = orderService.getAllOrdersByUserId(id, PageRequest.of(pageNum, pageSize));
        if (orders.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(
            @PathVariable Long id,
            @PathVariable Long orderId
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        List<OrderSimpleDTO> orders = userService.findDTOById(id).orders();
        if (orders.isEmpty()) return ResponseEntity.noContent().build();
        OrderDTO orderDTO = orderService.findDTOById(orderId);
        if (userService.getLoggedUserDTO().roles().contains("ADMIN")) return ResponseEntity.ok(orderDTO);
        for (OrderSimpleDTO order : orders){
            if (order.id() == orderId) return ResponseEntity.ok(orderDTO);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/{id}/orders/")
    public ResponseEntity<OrderSimpleDTO> createOrder(
            @PathVariable Long id,
            @RequestBody OrderFormtDTO orderFormDTO
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        OrderSimpleDTO order = orderService.proceedCheckout(orderFormDTO.paymentMethod(), orderFormDTO.address(), orderFormDTO.phoneNumber());
        if (order == null) return ResponseEntity.noContent().build();
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("{orderId}").buildAndExpand(order.id()).toUri();
        return ResponseEntity.created(location).body(order);
    }

    @DeleteMapping("/{id}/orders/{orderId}")
    public ResponseEntity<OrderDTO> deleteOrder(
            @PathVariable Long id,
            @PathVariable Long orderId
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        List<OrderSimpleDTO> orders = userService.findDTOById(id).orders();
        if (orders.isEmpty()) return ResponseEntity.noContent().build();
        if (userService.getLoggedUserDTO().roles().contains("ADMIN")){
            orderService.delete(orderId);
            return ResponseEntity.ok().build();
        }
        for (OrderSimpleDTO order : orders){
            if (order.id() == orderId){
                orderService.delete(orderId);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
