/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

 package es.dlj.onlinestore.controller;

 import java.net.MalformedURLException;

import org.springframework.http.HttpHeaders;
import java.nio.file.Paths;
import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
 import org.springframework.ui.Model;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PathVariable;
 import org.springframework.web.bind.annotation.RequestMapping;
 import ch.qos.logback.classic.Logger;
 import es.dlj.onlinestore.model.Product;
 import es.dlj.onlinestore.service.ProductService;
 import java.nio.file.Path;

 
 
 @Controller
 @RequestMapping("/image")
 class ImageController {
 
     private Logger log = (Logger) LoggerFactory.getLogger(getClass());
 
     @Autowired
     private ProductService productService;
 
     @GetMapping("/{productId}/{id}")
     public ResponseEntity<Object> loadProductImage(Model model,@PathVariable Long productId, @PathVariable Long id){
        Product product = productService.getProduct(productId);
        String imageName = product.getName()+"Image"+id;
        Path Images_Folder = Paths.get("images");
        UrlResource image = null;
        String contentType;
        contentType = product.getImages().get(id.intValue()-1).getContentType();
        Path imagePath = Images_Folder.resolve(imageName+contentType);
        log.info(contentType);
        try {
            image = new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (contentType == "jpg"){
            contentType = "jpeg";
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/"+contentType).body(image);
     }
 }