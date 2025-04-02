package es.dlj.onlinestore.controller.rest;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ProductSimpleDTO;
import es.dlj.onlinestore.dto.ProductTagDTO;
import es.dlj.onlinestore.dto.ProductTagSimpleDTO;
import es.dlj.onlinestore.dto.ReviewDTO;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.ReviewService;
import es.dlj.onlinestore.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/")
    public ResponseEntity<Collection<ProductSimpleDTO>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> productTypes,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) List<String> tags) {
        Collection<ProductSimpleDTO> products = productService.findAllDTOsBy(name, minPrice, maxPrice, tags, productTypes);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.findDTOById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/")
    public ResponseEntity<ProductDTO> createProduct(
            @RequestParam List<MultipartFile> imagesVal,
            @RequestParam String tagsVal,
            @Valid @RequestBody ProductDTO newProductDTO) {
        ProductDTO savedProductDTO = productService.saveProduct(newProductDTO, imagesVal, tagsVal);
        return ResponseEntity.status(201).body(savedProductDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestParam List<MultipartFile> imagesVal,
            @RequestParam String tagsVal,
            @Valid @RequestBody ProductDTO updatedProductDTO) {
        productService.updateProduct(id, updatedProductDTO, imagesVal, tagsVal);
        return ResponseEntity.ok(updatedProductDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.isProductOwner(id)) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping("/{id}/reviews/")
    public ResponseEntity<Void> addReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO) {
        reviewService.save(id, reviewDTO);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long productId, @PathVariable Long reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/add-to-cart")
    public ResponseEntity<Void> addProductToCart(@PathVariable Long id) {
        userService.addProductToCart(id);
        return ResponseEntity.status(201).build(); // Created
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Collection<ImageDTO>> getImagesProduct(@RequestParam Long id) {
        try {
            Collection<ImageDTO> images = productService.findDTOById(id).images();
            return ResponseEntity.ok(images);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/image/{imageId}")
    public ResponseEntity<Object> getImage(@PathVariable Long id, @PathVariable Long imageId) {
        try {
            return imageService.loadImage(imageId);
        } catch (NoSuchElementException e) {
            return imageService.loadDefaultImage();
        }
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<Object> getProductImage(@PathVariable Long id){
        try{
            return imageService.loadImage(productService.findDTOById(id).images().getFirst().id());
        }
        catch(NoSuchElementException e){
            return imageService.loadDefaultImage();
        }
    }

    /* 
    @PostMapping("/{id}/images")
    public ResponseEntity<Void> addImages (@RequestBody Long id, @RequestParam List<MultipartFile> imagesVal) {
        try {
            ProductDTO product = productService.findDTOById(id);
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            productService.saveImagesInProductDTO(product, imagesVal);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    } */

    @PutMapping("/images/{idImage}")
    public ResponseEntity<ImageDTO> updateImage(
            @PathVariable Long idImage,
            @Valid @RequestBody MultipartFile updatedImage) throws IOException {
        ImageDTO updatedI = imageService.updateImage(idImage, updatedImage);
        return ResponseEntity.ok(updatedI);
    }

    @DeleteMapping("/images/{idImage}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long idImage) {
        try {
            imageService.delete(imageService.findById(idImage));
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tags")
    public ResponseEntity<Collection<ProductTagDTO>> getAllTags() {
        Collection<ProductTagDTO> tags = productService.getAllTagsDTO();
        return ResponseEntity.ok(tags);
    }
    
    @GetMapping("/{productId}/tags")
    public ResponseEntity<Collection<ProductTagSimpleDTO>> getProductTag(@PathVariable Long id) {
        Collection<ProductTagSimpleDTO> tags = productService.findDTOById(id).tags();
        return ResponseEntity.ok(tags);        
    }

    @PostMapping("/tags")
    public ResponseEntity<ProductTagDTO> createTag(@PathVariable Long id, @Valid @RequestBody ProductTagDTO newTagDTO) {
        ProductTagDTO savedTagDTO = productService.saveTagDTO(newTagDTO);
        return ResponseEntity.status(201).body(savedTagDTO);
    }

}