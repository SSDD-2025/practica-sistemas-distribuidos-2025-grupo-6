package es.dlj.onlinestore.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.dlj.onlinestore.model.Image;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.ProductRepository;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.UserComponent;

 @Controller
 @RequestMapping("/image")
 class ImageController {

    @Autowired
    private ProductRepository productController;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private ImageService imageService;
 
    @GetMapping("/{id}")
    public ResponseEntity<Object> getImage(@PathVariable Long id){
        try {
            // Try to load the image
            return imageService.loadProductImage(id);
        } catch (Exception e) {
            // In case of error, return default image
            return loadDefaultImage();
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Object> getProductImage (@PathVariable Long id){
        // Find the product by its ID
        Optional<Product> product = productController.findById(id);
        if (!product.isPresent()) {
            // If the product does not exist, return 404
            return ResponseEntity.notFound().build();
        }

        try {
            // Try to load the image
            Image image = product.get().getImages().getFirst();
            return imageService.loadProductImage(image.getId());
        } catch (Exception e) {
            // In case of error, return default image
            return loadDefaultImage();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getUserImage (){
        // Get the user information
        UserInfo user = userComponent.getUser();
        try {
            // Try to load the image
            Image image = user.getProfileImage();
            return imageService.loadProductImage(image.getId());
        } catch (Exception e) {
            // In case of error, return default image
            return loadDefaultImage();
        }
    } 

    /**
     * Load the default image in case of error or no image found
     * @return The default image
     */
    private ResponseEntity<Object> loadDefaultImage() {
        try {
            // Load the default image
            Resource defaultImage = new ClassPathResource("static/images/default.png");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/png")
                    .contentLength(defaultImage.contentLength())
                    .body(defaultImage);
        } catch (IOException e) {
            // In case of error, return 404
            return ResponseEntity.notFound().build();
        }
    }
}
