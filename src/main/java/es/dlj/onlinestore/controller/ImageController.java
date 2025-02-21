package es.dlj.onlinestore.controller;

import org.springframework.http.ResponseEntity;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.dlj.onlinestore.model.Image;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.repository.ProductRepository;
import es.dlj.onlinestore.service.ImageService;


 
 
 @Controller
 @RequestMapping("/image")
 class ImageController {

    private Logger log = LoggerFactory.getLogger(ImageService.class);

    @Autowired
    private ProductRepository productController;

     @Autowired
     private ImageService imageService;
 
    @GetMapping("/{id}")
    public ResponseEntity<Object> getImage(@PathVariable Long id){
            return imageService.loadProductImage(id);
    }

    @GetMapping("/main/{id}")
    public ResponseEntity<Object> getMainImage (@PathVariable Long id){
        Optional<Product> product = productController.findById(id);
        log.info("Entrando en imagenes main");
        if (product.isPresent() && product != null){
            Image image = product.get().getImages().get(0);
            return imageService.loadProductImage(image.getId());
        }
        else
            return ResponseEntity.notFound().build(); 
    } 
}
