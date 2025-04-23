package es.dlj.onlinestore.controller.rest;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ProductFormDTO;
import es.dlj.onlinestore.dto.ProductSimpleDTO;
import es.dlj.onlinestore.dto.ProductTagDTO;
import es.dlj.onlinestore.dto.ProductTagSimpleDTO;
import es.dlj.onlinestore.dto.ReviewDTO;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.ReviewService;
import es.dlj.onlinestore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    private static final Map<String, String> EXTENSIONS = Map.of(
        ".jpg", "image/jpeg", 
        ".png", "image/png", 
        ".gif", "image/gif"
    );

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
    
    @Operation (summary = "Get all the products")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Products Found",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ProductSimpleDTO.class))}
        ),
        @ApiResponse(
            responseCode = "204",
            description = "Currently there are no products begin sold that fulfill your requirements.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Check the parameters you enter in the request, if there were any.",
            content = @Content
        )
    })
    @GetMapping("/")
    public ResponseEntity<Page<ProductSimpleDTO>> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> productTypes,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) List<String> tags,
            @RequestBody(required = false) Integer size,
            @RequestParam(required = false) Integer page
    ) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 8;
        Page<ProductSimpleDTO> products = productService.findAllDTOsBy(name, minPrice, maxPrice, tags, productTypes, PageRequest.of(pageNum, pageSize));
        if (products.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(products);
    }

    @Operation (summary = "Get product by id")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Product has been found",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ProductDTO.class))}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Wrong product id",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "After looking several times in our database we couldn't find the product with the id you provided.",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.findDTOById(id);
        return ResponseEntity.ok(product);
    }

    @Operation (summary = "Create a product")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "201",
            description = "Product Created",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ProductDTO.class))}
        ),
        
        @ApiResponse(
            responseCode = "400",
            description = "Check the new product's properties, some may not be properly written.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "You need to log in first.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Something unexpected happened, it is possible that the new product's properties are causing internal conflicts.",
            content = @Content
        )
    })
    @PostMapping("/")
    public ResponseEntity<ProductDTO> createProduct(
            @RequestBody ProductFormDTO productFormDTO
    ) {
        ProductDTO savedProductDTO = productService.saveProduct(productFormDTO.product(), null, productFormDTO.tagsVal());
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(savedProductDTO.id()).toUri();
        return ResponseEntity.created(location).body(savedProductDTO);
    }

    @Operation (summary = "Update a product")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Product updated",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ProductDTO.class))}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Check the new product's properties, some may not be properly written.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "You need to log in first.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "You can only update the product if you are its seller or an administrator.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Are you sure the product with that id exists?",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Something unexpected happened, it is possible that the altered properties are causing internal conflicts.",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductFormDTO productFormDTO) {
        if (isActionAllowed(id, "product")) { 
            ProductDTO updatedProduct = productService.updateProduct(id, productFormDTO.product(), null, productFormDTO.tagsVal());
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.status(403).build(); 
        }
    }

    @Operation (summary = "Update a product")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Product deleted",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ProductDTO.class))}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Check the id provided.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "You need to log in first.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "You can only delete the product if you are its seller or an administrator.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "These are good news as well, the product you try to delete doesn't exist already.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Trying to delete the product caused an internal problem.",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        if (isActionAllowed(id, "product")) {
            ProductDTO productDTO = productService.deleteProduct(id);
            return ResponseEntity.ok(productDTO);
        }
        return ResponseEntity.status(403).build();
    }

    /*
    @GetMapping("/{id}/reviews")
    public ResponseEntity<Collection<ReviewDTO>> getReviews(@PathVariable Long id) {
        Collection<ReviewDTO> reviews = reviewService.findAllByProductId(id);
        return ResponseEntity.ok(reviews);
    } */

    @Operation (summary = "Get reviews from a product")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Reviews found.",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ReviewDTO.class))}
        ),
        @ApiResponse(
            responseCode = "204",
            description = "This product doesn't have reviews",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "You sure the product's id is correct?",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Are you sure the product with that id exists?",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Something unexpected happened while loading the product's reviews.",
            content = @Content
        )
    })
    @GetMapping("/{id}/reviews/")
    public ResponseEntity<Page<ReviewDTO>> getReviews(@PathVariable Long id, Pageable pageable) {
                        
        Page<ReviewDTO> reviewsPage = reviewService.findAllByProductIdPag(id, pageable);
        if (reviewsPage.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(reviewsPage);
    }

    @Operation (summary = "Get a review from a product through their ids")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Review found.",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ReviewDTO.class))}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Are you sure the product's and review's id are correct?",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Either the product or the review exist, maybe the review belongs to another product?",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "We had problems loading the review.",
            content = @Content
        )
    })
    @GetMapping("/{id}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long id, @PathVariable Long reviewId) {
        try {
            ReviewDTO review = reviewService.findById(reviewId);
            return ResponseEntity.ok(review);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation (summary = "Add a review to a product.")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "201",
            description = "Review added to product.",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ReviewDTO.class))}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Check the review's properties.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "We value your opinion, but you need to log in first.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "I think you didn't provide us with the right product id",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Something unexpected happened, it is possible that the review's properties are causing internal conflicts.",
            content = @Content
        )
    })
    @PostMapping("/{id}/reviews/")
    public ResponseEntity<ReviewDTO> addReview(
            @PathVariable Long id, 
            @Valid @RequestBody ReviewDTO reviewDTO
        ) {
        ReviewDTO savedReviewDTO = reviewService.save(id, reviewDTO);
        URI location = fromCurrentRequest().path("/{reviewId}").buildAndExpand(savedReviewDTO.id()).toUri();
        return ResponseEntity.created(location).body(savedReviewDTO);
    }

    @Operation (summary = "Delete a review from a product.")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Review deleted.",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ReviewDTO.class))}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid review or product id.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "You need to log in first.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "You can only delete the review if you are the author or an administrator.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Either the review has already been deleted or the product does no longer exist.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "There was a problem while deleting the review.",
            content = @Content
        )
    })
    @DeleteMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> deleteReview(@PathVariable Long productId, @PathVariable Long reviewId) {
        if (isActionAllowed(reviewId, "review")){
        ReviewDTO review = reviewService.delete(reviewId);
        return ResponseEntity.ok(review);
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation (summary = "Adding a product to the cart.")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Product added to shopping cart.",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ProductSimpleDTO.class))}
        ),
        @ApiResponse(
            responseCode = "201",
            description = "Cart created and product added.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid product id.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "401",
            description = "You need to log in first before buying.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "What do you want to add to the shopping cart? Air? Just kidding with product the id provided doesn't exist.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "There was a problem adding the product to your cart.",
            content = @Content
        )
    })
    @PostMapping("/{id}/cart/")
    public ResponseEntity<Object> addProductToCart(@PathVariable Long id) {
        UserDTO userDTO = userService.getLoggedUserDTO();
        userDTO = userService.addProductToCart(id);
        ProductDTO productDTO = productService.findDTOById(id);
        if (userDTO.cartProducts().size()>1) return ResponseEntity.ok(productDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{id}/cart").buildAndExpand(userDTO.id()).toUri();
        return ResponseEntity.created(location).body(userDTO.cartProducts()); // Created
    }

    @Operation (summary = "Getting the images of a product.")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Product's images found.",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ImageDTO.class))}
        ),
        @ApiResponse(
            responseCode = "204",
            description = "The product doesn't have images.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid product id.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "This product doesn't have images, or the product itself doesn't exist.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "There was a problem while loading the images information.",
            content = @Content
        )
    })
    @GetMapping("/{id}/images/")
    public ResponseEntity<Collection<ImageDTO>> getImagesProduct(@PathVariable Long id) {
        try {
            ProductDTO productDTO = productService.findDTOById(id);
            if (productDTO.images().isEmpty()) return ResponseEntity.noContent().build();
            return ResponseEntity.ok(productDTO.images());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation (summary = "Getting an image of a product.")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Product's image found.",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ImageDTO.class))}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid product or image id.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "The image you are looking for could not be found.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "There was a problem while loading the image.",
            content = @Content
        )
    })
    @GetMapping("/{id}/images/{imageId}")
    public ResponseEntity<Object> getImage(@PathVariable Long id, @PathVariable Long imageId) {
        try {
            ImageDTO imageDTO = imageService.findById(imageId);
            Resource imageAPI = imageService.loadAPIImage(imageDTO.id());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, EXTENSIONS.get(imageDTO.contentType())).body(imageAPI);
        }catch (NoSuchElementException e) {
            return imageService.loadDefaultImage();
        }
    }

    @Operation (summary = "Adding an image to a product.")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "201",
            description = "Image uploaded.",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ImageDTO.class))}
        ),
        @ApiResponse(
            responseCode = "401",
            description = "You need to log in first.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "In order to upload the product's image you must be the product's seller or an administrator.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid product id.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "The product related to the image doesn't exist.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "There was a problem while uploading the image.",
            content = @Content
        )
    })
    @PostMapping("/{id}/images/")
    public ResponseEntity<Object> addImage (@PathVariable Long id, @RequestBody MultipartFile image) throws IOException {
        if (!isActionAllowed(id, "product")) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        try {
            ImageDTO imageDTO = productService.addImage(id, image);
            URI location = fromCurrentRequest().path("/{id}").buildAndExpand(imageDTO.id()).toUri();
            return ResponseEntity.created(location).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    } 
 
    @Operation (summary = "Updating an image of a product.")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Image updated.",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ImageDTO.class))}
        ),
        @ApiResponse(
            responseCode = "401",
            description = "You need to log in first.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "In order to update the product's image you must be the product's seller or an administrator.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid product id or imageId.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "The product and the image haven't been found.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "There was a problem while updated the image.",
            content = @Content
        )
    })
    @PutMapping("{id}/images/{idImage}")
    public ResponseEntity<Object> updateImage(
            @PathVariable Long id,
            @PathVariable Long idImage,
            @Valid @RequestBody MultipartFile updatedImage
    ) throws IOException {
        if (!isActionAllowed(id, "product")) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        try {
            ImageDTO imageDTO = imageService.updateImage(idImage, updatedImage);
            Resource imageAPI = imageService.loadAPIImage(imageDTO.id());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, EXTENSIONS.get(imageDTO.contentType())).body(imageAPI);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation (summary = "Deleting an image of a product.")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Image deleted.",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ImageDTO.class))}
        ),
        @ApiResponse(
            responseCode = "401",
            description = "You need to log in first.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "In order to delete the product's image you must be the product's seller or an administrator.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid product id or imageId.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "The product and the image haven't been found or isn't related to the product given.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "There was a problem while deleting the image.",
            content = @Content
        )
    })
    @DeleteMapping("{id}/images/{idImage}")
    public ResponseEntity<ImageDTO> deleteImage(
            @PathVariable Long idImage,
            @PathVariable Long id
    ) {
        if (!isActionAllowed(id, "product")) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        try {
            ImageDTO imageDTO = productService.deleteImage(imageService.findById(idImage), id);
            return ResponseEntity.ok(imageDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation (summary = "Getting all product tags.")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Tags updated.",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ProductTagDTO.class))}
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No tags found.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "There was a problem loading the product tags.",
            content = @Content
        )
    })
    @GetMapping("/tags/")
    public ResponseEntity<Collection<ProductTagDTO>> getAllTags() {
        Collection<ProductTagDTO> tags = productService.getAllTagsDTO();
        return ResponseEntity.ok(tags);
    }
    
    @Operation (summary = "Getting the product's tags.")
    @ApiResponses(value={
        @ApiResponse(
            responseCode = "200",
            description = "Tags found.",
            content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=ProductTagDTO.class))}
        ),
        @ApiResponse(
            responseCode = "204",
            description = "This product doesn't have tags.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid product id.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found with the provided id.",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description = "There was a problem loading the product tags.",
            content = @Content
        )
    })
    @GetMapping("/{id}/tags/")
    public ResponseEntity<Collection<ProductTagSimpleDTO>> getProductTag(@PathVariable Long id) {
        Collection<ProductTagSimpleDTO> tags = productService.findDTOById(id).tags();
        if (tags.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(tags);        
    }
        
}