package es.dlj.onlinestore.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.dlj.onlinestore.domain.Image;
import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.mapper.ImageMapper;
import es.dlj.onlinestore.mapper.ProductMapper;
import es.dlj.onlinestore.mapper.UserMapper;
import es.dlj.onlinestore.repository.ImageRepository;



@Service
public class ImageService {
    
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Transactional
    public ImageDTO saveFileImage(MultipartFile rawImage) throws IOException {
        Image image = new Image();
        image.setContentType(rawImage.getContentType());
        byte[] imageData = rawImage.getBytes();
        image.setImageFile(BlobProxy.generateProxy(new ByteArrayInputStream(imageData), imageData.length));
        return imageMapper.toDTO(imageRepository.save(image));
    }

    @Transactional
    public Image saveFileImageFromPath(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File doesn't exists: " + filePath);
        }
        byte[] imageData = Files.readAllBytes(file.toPath());
        String contentType = Files.probeContentType(file.toPath());
        Image image = new Image();
        image.setContentType(contentType != null ? contentType : "image/png");
        image.setImageFile(BlobProxy.generateProxy(new ByteArrayInputStream(imageData), imageData.length));
        return imageRepository.save(image);
    }

    @Transactional
    public void saveImagesInProduct(Product product, List<MultipartFile> rawImages) throws IOException {
        product.clearImages();
        for (MultipartFile rawImage : rawImages){
            Image savedImage = imageMapper.toDomain(saveFileImage(rawImage));
            product.addImage(savedImage);
        }
    }

    @Transactional
    public UserDTO saveImageInUser(UserDTO userDTO, MultipartFile rawImage) throws IOException {
        Image savedImage = imageMapper.toDomain(saveFileImage(rawImage));
        User user = userMapper.toDomain(userDTO);

        user.setProfilePhoto(savedImage);
        return userMapper.toDTO(userService.saveUser(user));
    }

    public ResponseEntity<Object> loadImage(Long id) {
        try {
            Image image = imageRepository.findById(id).get();
            Blob imageData = image.getimageFile();
            Resource imageFile = new InputStreamResource(imageData.getBinaryStream());
            String contentType = "image/" + image.getContentType();
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, contentType).contentLength(imageData.length()).body(imageFile);
        } catch (SQLException | NoSuchElementException e) {
            return loadDefaultImage();
        }
    }

    public void deleteImage(ImageDTO profilePhoto) {
        imageRepository.delete(imageMapper.toDomain(profilePhoto));
    }

    void deleteImage(Image profilePhoto) {
        imageRepository.delete(profilePhoto);
    }

    public ResponseEntity<Object> loadImageFromPath(String path, String contentType) {
        try {
            Resource defaultImage = new ClassPathResource(path);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, contentType)
                    .contentLength(defaultImage.contentLength()).body(defaultImage);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Object> loadDefaultImage() {
        return loadImageFromPath("static/images/default.png", "image/png");
    }

    @Transactional
    void delete(Image image) {
        imageRepository.delete(image);
    }
}