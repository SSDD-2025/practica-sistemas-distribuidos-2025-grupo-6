package es.dlj.onlinestore.controller.rest;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.OrderSimpleDTO;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ProductSimpleDTO;
import es.dlj.onlinestore.dto.ReviewDTO;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.dto.UserFormDTO;
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

    @GetMapping("/")
    public UserDTO getUser(){
        return userService.getLoggedUserDTO();
    }

    @PostMapping("/")
    public ResponseEntity<Object> registerUser(@RequestBody UserFormDTO user, @RequestBody (required = false) MultipartFile image, BindingResult bindingResult, UriBuilder uriBuilder){
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
                return ResponseEntity.internalServerError().build();
            }
        }
        URI location =  fromCurrentRequest().build().toUri();

        return ResponseEntity.created(location).body(userDTO);
    }

    @PutMapping("/")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO newUserDTO, @RequestBody(required=false) MultipartFile profilePhotoFile, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
           for (FieldError error : bindingResult.getFieldErrors()) {
               errors.put(error.getField(), error.getDefaultMessage());
           }
           return ResponseEntity.badRequest().body(errors);
       }
       UserDTO userDTO = userService.getLoggedUserDTO();

       boolean isUsernameChanged = !userDTO.username().equals(newUserDTO.username());

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

    @DeleteMapping("/")
    public ResponseEntity<?> deleteUser(){
        UserDTO userDTO = userService.getLoggedUserDTO();
        if (userDTO == null) return ResponseEntity.badRequest().body("Login first in order to delete the account.");
        userService.deleteDTOById(userDTO.id());
        return ResponseEntity.ok("User" + userDTO.username() + "deleted successfuly.");
    }

    @GetMapping("/products")
    public List<ProductSimpleDTO> getSellingProducts(){
        UserDTO userDTO = userService.getLoggedUserDTO();
        return userDTO.productsForSell();
    }

    /** @PostMapping("/products") el añadir un producto a los productos vendidos por el usuario
     * debería de implementarse cuando se crea un producto en ProductRestController **/
    
    // @PutMapping("/products") lo mismo cuando se edita un producto

    @GetMapping("/reviews")
    public List<ReviewDTO> getReviews(){
        UserDTO userDTO = userService.getLoggedUserDTO();
        return userDTO.reviews();
    }

    @GetMapping("/image")
    public ImageDTO getProfileImage(){
        UserDTO userDTO = userService.getLoggedUserDTO();
        return userDTO.profilePhoto();
    }

    @PostMapping("/image")
    public ImageDTO addProfileImage(@RequestBody MultipartFile imageFile){
        UserDTO userDTO = userService.getLoggedUserDTO();
        try {
            userDTO = imageService.saveImageInUser(imageFile);
        } catch (IOException e) {
            
            e.printStackTrace();
        }
        return userDTO.profilePhoto();
    }

    @PutMapping("/image")
    public ImageDTO updateProfileImage (@RequestBody MultipartFile imageFile){
        UserDTO userDTO = userService.getLoggedUserDTO();
        ImageDTO oldPhotoDTO = userDTO.profilePhoto();
        try {
            userDTO = imageService.saveImageInUser(imageFile);
            if (oldPhotoDTO != null) imageService.delete(oldPhotoDTO);
        } catch (IOException e) {
            
            e.printStackTrace();
        }
        return userDTO.profilePhoto();
    }

    @DeleteMapping("/image")
    public void deleteProfileImage(){
        UserDTO userDTO = userService.getLoggedUserDTO();
        ImageDTO oldPhotoDTO = userDTO.profilePhoto();
        if (oldPhotoDTO != null) imageService.delete(oldPhotoDTO);
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
}
