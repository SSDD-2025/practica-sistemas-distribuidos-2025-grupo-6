package es.dlj.onlinestore.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import es.dlj.onlinestore.model.Image;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.repository.ImageRepository;



@Service
public class ImageService {
    
    @Autowired
    private ImageRepository images;

    @Transactional //without this it will give ERROR 8172 [nio-8080-exec-3] o.h.engine.jdbc.spi.SqlExceptionHelper   : Underlying stream does not allow reset
    public void saveImages(Product product, List<MultipartFile> rawImages) throws IOException {
        product.clearImages();
        for (MultipartFile rawImage : rawImages){
            Image image = new Image();
            image.setContentType(rawImage.getContentType());
            byte[] imageData = rawImage.getBytes();
            image.setimageFile(BlobProxy.generateProxy(new ByteArrayInputStream(imageData), imageData.length));
            product.addImage(image);
            images.save(image);
        }
    }

    public ResponseEntity<Object> loadProductImage(Long id) throws ResourceAccessException {
        Optional<Image> image = images.findById(id);
        if (!image.isPresent()){
            throw new ResourceAccessException("Image not found");
        }
        Blob imageData = image.get().getimageFile();
        try {
            Resource imageFile = new InputStreamResource(imageData.getBinaryStream());
            String contentType = "image/"+image.get().getContentType();
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, contentType).contentLength(imageData.length()).body(imageFile);
        } catch (SQLException e) {
            return ResponseEntity.notFound().build();
        }
    }
}