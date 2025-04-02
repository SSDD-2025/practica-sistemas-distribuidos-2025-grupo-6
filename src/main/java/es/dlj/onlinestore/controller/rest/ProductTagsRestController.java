package es.dlj.onlinestore.controller.rest;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ProductSimpleDTO;
import es.dlj.onlinestore.dto.ProductTagDTO;
import es.dlj.onlinestore.dto.ReviewDTO;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.ReviewService;
import es.dlj.onlinestore.service.UserService;
import jakarta.validation.Valid;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;


@RestController
@RequestMapping("/api/tags")
public class ProductTagsRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/")
    public ResponseEntity<Collection<ProductTagDTO>> getAllProductTags() {
        Collection<ProductTagDTO> productTags = productService.findAllTagsDTOs();
        return ResponseEntity.ok(productTags);
    }

    // @PostMapping("/")
	// public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {

	// 	bookDTO = bookService.createBook(bookDTO);

	// 	URI location = fromCurrentRequest().path("/{id}").buildAndExpand(bookDTO.id()).toUri();

	// 	return ResponseEntity.created(location).body(bookDTO);
	// }

    @PostMapping("/")
    public ResponseEntity<ProductTagDTO> postProductTag(@RequestBody ProductTagDTO productTagDTO) {
        ProductTagDTO savedProductTag = productService.saveTagDTO(productTagDTO);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(savedProductTag.id()).toUri();
		return ResponseEntity.created(location).body(savedProductTag);
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

    @GetMapping("/image")
    public ResponseEntity<Object> getProductImage(@PathVariable Long id){
        try{
            return imageService.loadImage(productService.findDTOById(id).images().getFirst().id());
        }
        catch(NoSuchElementException e){
            return imageService.loadDefaultImage();
        }
    }
}