package es.dlj.onlinestore.service;

import java.io.IOException;
import java.sql.Blob;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.dlj.onlinestore.model.Image;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.repository.ImageRepository;

@Service
public class ImageService {
    @Autowired
    private ImageRepository images;

    public void saveImage(Product product, MultipartFile rawImage, int id){
        if (rawImage != null && !rawImage.isEmpty()){

            Image image = new Image();
            image.setContentType(rawImage.getContentType()); 
            image.setFileName(product.getName()+"_image_"+id);
            try {
                image.setimageFile(BlobProxy.generateProxy(rawImage.getInputStream(), rawImage.getSize()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            product.addImage(image);
            images.save(image);
        }
    }

    
}
