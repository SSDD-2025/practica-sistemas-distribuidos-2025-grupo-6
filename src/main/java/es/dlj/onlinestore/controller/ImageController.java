package es.dlj.onlinestore.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ch.qos.logback.classic.Logger;
import es.dlj.onlinestore.model.Image;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.service.ProductService;

 
 
 @Controller
 @RequestMapping("/image")
 class ImageController {
 
     private Logger log = (Logger) LoggerFactory.getLogger(getClass());
 
     @Autowired
     private ProductService productService;
 
     @GetMapping("/{productId}/{id}")
     public ResponseEntity<Object> loadProductImage(Model model, @PathVariable Long id, @PathVariable Long productId){
        Optional <Product> product = productService.findById(productId);
        if (product.isPresent()){
            List <Image> images = product.get().getImages();
            if (images != null){
                Image image = images.get(id.intValue() - 1);
                Blob imageFile = image.getimageFile();
                try {
                    InputStreamResource file = new InputStreamResource(imageFile.getBinaryStream());
                    return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/"+image.getContentType()).contentLength(imageFile.length()).body(imageFile);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResponseEntity.notFound().build();
    }
}