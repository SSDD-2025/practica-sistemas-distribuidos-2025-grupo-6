package es.dlj.onlinestore.service;

import java.io.IOException;
import java.sql.Blob;
import java.util.HashSet;
import java.util.Optional;
import java.sql.SQLException;


import org.hibernate.engine.jdbc.BlobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.InputStreamResource;

import es.dlj.onlinestore.model.Image;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.repository.ImageRepository;



@Service
public class ImageService {

    private Logger log = LoggerFactory.getLogger(ImageService.class);
    
    @Autowired
    private ImageRepository images;

    public void saveImage(Product product, MultipartFile rawImage, int id, boolean mainImage){
        log.info(product.getName());
        log.info(rawImage.getOriginalFilename());
        if (rawImage != null && !rawImage.isEmpty()){
            Image image = new Image();
            image.setContentType(rawImage.getContentType()); 
            image.setFileName(product.getName()+"_image_"+id);
            if (mainImage){
                image.setIsMainImage(true);
            }
            try {
                image.setimageFile(BlobProxy.generateProxy(rawImage.getInputStream(), rawImage.getSize()));
                log.info("es imagen principal: " + image.getIsMainImage());
            } catch (IOException e) {
                e.printStackTrace();
            }
            product.addImage(image);
            images.save(image);
            log.info("Imagen: "+product.getImages().size());
        }
    }

    public ResponseEntity<Object> loadProductImage(Long id){
            Optional<Image> image = images.findById(id);
            if (image.isPresent() && image != null){
                Blob imageData = image.get().getimageFile();
                try {
                    Resource imageFile = new InputStreamResource(imageData.getBinaryStream());
                    String contentType = "image/"+image.get().getContentType();
                    log.info("Id imagen: " + id);
                    return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, contentType).contentLength(imageData.length()).body(imageFile);
                } catch (SQLException e) {
                    log.info("Error al cargar");
                }
            }
        return ResponseEntity.notFound().build();
    }
}
