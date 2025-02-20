package es.dlj.onlinestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.repository.ImageRepository;

@Service
public class ImageService {
    @Autowired
    private ImageRepository images;

    public void saveImage(Product product, MultipartFile image){
        
    }
    
}
