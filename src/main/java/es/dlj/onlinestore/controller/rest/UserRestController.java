package es.dlj.onlinestore.controller.rest;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.OrderSimpleDTO;
import es.dlj.onlinestore.dto.ProductSimpleDTO;
import es.dlj.onlinestore.dto.ReviewDTO;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.dto.UserFormDTO;
import es.dlj.onlinestore.dto.UserSimpleDTO;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.OrderService;
import es.dlj.onlinestore.service.UserService;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired 
    OrderService orderService;

    @Autowired
    private ImageService imageService;

    private boolean isActionAllowed(Long id){
        UserDTO userDTO = userService.getLoggedUserDTO();
        return userDTO.roles().contains("ADMIN") || userDTO.id() == id;   
    }

    @GetMapping("/")
    public ResponseEntity<Collection<UserSimpleDTO>> getUsers(){
        Collection<UserSimpleDTO> users = userService.getAllUsers();
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.badRequest().body(null);
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
            BindingResult bindingResult,
            @RequestBody (required = false) MultipartFile image
    ){
        if (bindingResult.hasErrors()){
             Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        UserDTO userDTO = userService.saveDTO(user);
        if (image!= null && !image.isEmpty()){
            try {
                userDTO = imageService.saveImageInUser(image);
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error while saving the profile image.");
            }
        }
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(userDTO.id()).toUri();

        return ResponseEntity.created(location).body(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(
            @Valid @RequestBody UserDTO newUserDTO,
            BindingResult bindingResult,
            @PathVariable Long id,
            @RequestBody(required=false) MultipartFile profilePhotoFile
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

       //TODO: boolean isUsernameChanged = !userDTO.username().equals(newUserDTO.username());

        userDTO = userService.update(userDTO.id(), newUserDTO);
        
        if (profilePhotoFile != null) {
            try {
                if (!profilePhotoFile.isEmpty()) {
                    ImageDTO oldPhotoDTO = userDTO.profilePhoto();
                    imageService.saveImageInUser(profilePhotoFile);
                    if (oldPhotoDTO != null) imageService.delete(oldPhotoDTO);
                }
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error while working with image: " + e.getMessage());
            }
        }
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
            UserDTO userDTO = userService.findDTOById(id);
            userService.deleteDTOById(userDTO.id());
            return ResponseEntity.ok("User" + userDTO.username() + "deleted successfuly.");
        } catch (Error e) {
            return ResponseEntity.badRequest().body("Login first in order to delete the account.");
        }
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<Set<ProductSimpleDTO>> getSellingProducts(
        @PathVariable Long id
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        UserDTO userDTO = userService.findDTOById(id);
        return ResponseEntity.ok(userDTO.cartProducts());
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviews(
        @PathVariable Long id
    ){
        if (isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        UserDTO userDTO = userService.findDTOById(id);
        return ResponseEntity.ok(userDTO.reviews());
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<ImageDTO> getProfileImage(
        @PathVariable Long id
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        UserDTO userDTO = userService.findDTOById(id);
        return ResponseEntity.ok(userDTO.profilePhoto());
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<ImageDTO> addProfileImage(
            @RequestBody MultipartFile imageFile,
            @PathVariable Long id
        ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        UserDTO userDTO = userService.getLoggedUserDTO();
        try {
            userDTO = imageService.saveImageInUser(imageFile);
            return ResponseEntity.ok(userDTO.profilePhoto());

        } catch (IOException e) {
            
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/id/image")
    public ResponseEntity<ImageDTO> updateProfileImage (
            @RequestBody MultipartFile imageFile,
            @PathVariable Long id
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        UserDTO userDTO = userService.findDTOById(id);
        ImageDTO oldPhotoDTO = userDTO.profilePhoto();
        try {
            userDTO = imageService.saveImageInUser(imageFile);
            if (oldPhotoDTO != null) imageService.delete(oldPhotoDTO);
            return ResponseEntity.ok(userDTO.profilePhoto());
        } catch (IOException e) {
            
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<?> deleteProfileImage(
        @PathVariable Long id
    ){
        if (!isActionAllowed(id)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        UserDTO userDTO = userService.getLoggedUserDTO();
        ImageDTO oldPhotoDTO = userDTO.profilePhoto();
        if (oldPhotoDTO != null) imageService.delete(oldPhotoDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cart")
    public Collection<ProductSimpleDTO> getCart(){
        UserDTO userDTO = userService.getLoggedUserDTO();
        return userDTO.cartProducts();
    }

    @PostMapping("/cart/product/{id}")
    public ResponseEntity<?> addProductToCart(@PathVariable Long id){
        
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

    @PostMapping("/cart/order")
    public ResponseEntity<OrderSimpleDTO> createOrder(@RequestBody String paymentMethod, String address, String phoneNumber, UriComponentsBuilder  uriBuilder){
        OrderSimpleDTO order = orderService.proceedCheckout(paymentMethod, address, phoneNumber);
        if (order == null) return ResponseEntity.noContent().build();
        URI location =  uriBuilder.path("/api/profile/order/{id}").buildAndExpand(order.id()).toUri();
        return ResponseEntity.created(location).body(order);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser (
        @RequestBody String username, 
        @RequestBody String password
    ){
        //TODO: userService.
        return null;
    }
}
