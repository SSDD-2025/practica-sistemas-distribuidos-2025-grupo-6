package es.dlj.onlinestore.controller.rest;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.data.domain.Page; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ProductSimpleDTO;
import es.dlj.onlinestore.dto.ProductTagDTO;
import es.dlj.onlinestore.dto.ProductTagSimpleDTO;
import es.dlj.onlinestore.dto.ReviewDTO;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.ReviewService;
import es.dlj.onlinestore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

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

    private boolean isActionAllowed(Long id, String entityAffected){
        UserDTO userDTO = userService.getLoggedUserDTO();
        if (userDTO.roles().contains("ADMIN")) return true;
        switch (entityAffected) {
            case "review":
                ReviewDTO reviewDTO = reviewService.findById(id);
                for (ReviewDTO userReviewDTO: userDTO.reviews()){
                    if (userReviewDTO.id() == reviewDTO.id()) return true;
                }
                return false;
            case "product":
                ProductDTO productDTO = productService.findDTOById(id);
                return userDTO.id() == productDTO.seller().id();
            default:
                return false;
        }
    }

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
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(savedProductDTO.id()).toUri();
        return ResponseEntity.created(location).body(savedProductDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestParam List<MultipartFile> imagesVal,
            @RequestParam String tagsVal,
            @Valid @RequestBody ProductDTO updatedProductDTO) {
        if (isActionAllowed(id, "product")) { 
            productService.updateProduct(id, updatedProductDTO, imagesVal, tagsVal);
            return ResponseEntity.ok(updatedProductDTO);
        } else {
            return ResponseEntity.status(403).build(); 
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (isActionAllowed(id, "product")) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(403).build();
    }

    /*
    @GetMapping("/{id}/reviews")
    public ResponseEntity<Collection<ReviewDTO>> getReviews(@PathVariable Long id) {
        Collection<ReviewDTO> reviews = reviewService.findAllByProductId(id);
        return ResponseEntity.ok(reviews);
    } */

    @GetMapping("/{id}/reviews")
    public ResponseEntity<Page<ReviewDTO>> getReviews(@PathVariable Long id, Pageable pageable) {
                        
        Page<ReviewDTO> reviewsPage = reviewService.findAllByProductIdPag(id, pageable);
        return ResponseEntity.ok(reviewsPage);
    }

    @GetMapping("/{id}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long id, @PathVariable Long reviewId) {
        try {
            ReviewDTO review = reviewService.findById(reviewId);
            return ResponseEntity.ok(review);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/reviews/")
    public ResponseEntity<ReviewDTO> addReview(
            @PathVariable Long id, 
            @Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO savedReviewDTO = reviewService.save(id, reviewDTO);
        URI location = fromCurrentRequest().path("/{reviewId}").buildAndExpand(savedReviewDTO.id()).toUri();
        return ResponseEntity.created(location).body(savedReviewDTO);
    }

    @DeleteMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long productId, @PathVariable Long reviewId) {
        if (!isActionAllowed(reviewId, "review"))
        reviewService.delete(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cart")
    public ResponseEntity<Set<ProductSimpleDTO>> addProductToCart(@PathVariable Long id) {
        UserDTO userDTO = userService.getLoggedUserDTO();
        userDTO = userService.addProductToCart(id);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{id}/cart").buildAndExpand(userDTO.id()).toUri();
        return ResponseEntity.created(location).body(userDTO.cartProducts()); // Created
    }

    @GetMapping("/{id}/images/")
    public ResponseEntity<Collection<ImageDTO>> getImagesProduct(@RequestParam Long id) {
        try {
            Collection<ImageDTO> images = productService.findDTOById(id).images();
            return ResponseEntity.ok(images);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/images/{imageId}")
    public ResponseEntity<Object> getImage(@PathVariable Long id, @PathVariable Long imageId) {
        try {
            return imageService.loadImage(imageId);
        } catch (NoSuchElementException e) {
            return imageService.loadDefaultImage();
        }
    }

    // @GetMapping("/{id}/firstimage")
    // public ResponseEntity<Object> getProductFirstImage(@PathVariable Long id){
    //     try{
    //         return imageService.loadImage(productService.findDTOById(id).images().getFirst().id());
    //     }
    //     catch(NoSuchElementException e){
    //         return imageService.loadDefaultImage();
    //     }
    // }

    //TODO: hay que hacer que guarde las nuevas imagenes en el producto.
    @PostMapping("/{id}/images/")
    public ResponseEntity<List<ImageDTO>> addImage (@RequestBody Long id, @RequestParam List<MultipartFile> imagesVal) throws IOException {
        if (!isActionAllowed(id, "product")) return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        try {
            for (MultipartFile image : imagesVal) {
                imageService.saveFileImage(image);
            }
            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/products/{id}").buildAndExpand(id).toUri();
            return ResponseEntity.created(location).body(null);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    } 

    //TODO: explicar finalidad
    //TODO: mal construida
    @PutMapping("/images/{idImage}")
    public ResponseEntity<ImageDTO> updateImage(
            @PathVariable Long idImage,
            @Valid @RequestBody MultipartFile updatedImage
    ) throws IOException {
        ImageDTO updatedI = imageService.updateImage(idImage, updatedImage);
        return ResponseEntity.ok(updatedI);
    }

    //TODO: añadir seguridad para verificar que el usuario es dueño de la imagen
    // TODO: mala construcción
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


    // TODO: Esto debemos permitirlo??
    @PostMapping("/tags")
    public ResponseEntity<ProductTagDTO> createTag(@PathVariable Long id, @Valid @RequestBody ProductTagDTO newTagDTO) {
        ProductTagDTO savedTagDTO = productService.saveTagDTO(newTagDTO);
        return ResponseEntity.status(201).body(savedTagDTO);
    }
        

}