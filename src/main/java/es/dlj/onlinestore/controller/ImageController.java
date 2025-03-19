package es.dlj.onlinestore.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.dlj.onlinestore.model.Image;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.UserInfo;
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
        return imageService.loadImage(id);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Object> getProductImage(@PathVariable Long id) {
        try {
            Product product = productService.getProduct(id);
            return imageService.loadImage(product.getImages().getFirst().getId());
        } catch (NoSuchElementException e) {
            return imageService.loadDefaultImage();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getUserImage(HttpServletRequest request) {
        UserInfo user = userService.getLoggedUser(request);
        if (user == null) return imageService.loadDefaultImage();

        Image image = user.getProfilePhoto();
        if (image == null) return imageService.loadDefaultImage();
        return imageService.loadImage(image.getId());
    }
}
