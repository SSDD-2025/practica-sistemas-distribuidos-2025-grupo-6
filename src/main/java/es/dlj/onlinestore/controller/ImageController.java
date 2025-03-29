package es.dlj.onlinestore.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

 @Controller
 @RequestMapping("/image")
 class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;
 
    @GetMapping("/{id}")
    public ResponseEntity<Object> getImage(@PathVariable Long id) {
        ResponseEntity<Object> image = imageService.loadImage(id);
        return image;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Object> getProductImage(@PathVariable Long id) {
        try {
            ProductDTO product = productService.getProduct(id);
            return imageService.loadImage(product.images().getFirst().id());
        } catch (NoSuchElementException e) {
            return imageService.loadDefaultImage();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getUserImage(HttpServletRequest request) {
        UserDTO user = userService.getLoggedUser(request);
        if (user == null) return imageService.loadDefaultImage();

        ImageDTO image = user.profilePhoto();
        if (image == null) return imageService.loadDefaultImage();
        return imageService.loadImage(image.id());
    }
}
