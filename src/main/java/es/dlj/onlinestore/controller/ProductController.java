package es.dlj.onlinestore.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.dlj.onlinestore.domain.Image;
import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.domain.ProductTag;
import es.dlj.onlinestore.domain.Review;
import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.enumeration.ProductType;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.ReviewService;
import es.dlj.onlinestore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/product")
class ProductController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            User user = userService.findByUserName(principal.getName()).get();
            model.addAttribute("user", user);
            model.addAttribute("isLogged", true);
            model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
            model.addAttribute("isUser", request.isUserInRole("USER"));

        } else {
            model.addAttribute("isLogged", false);
        }
    }

    @GetMapping("/{id}")
    public String loadProductDetails(Model model, @PathVariable Long id){
        Product product = productService.getProduct(id);

        // Create a list of maps to store image names and stablish if has to be shown as first
        List<Map<String, Object>> images = new ArrayList<>();
        List<Image> productImages = product.getImages();
        for (int i = 0; i < productImages.size(); i++) {
            images.add(Map.of("name", productImages.get(i).getId(), "selected", (i == 0)));
        }

        model.addAttribute("allImages", images);
        // model.addAttribute("user", userComponent.getUser());
        model.addAttribute("product", productService.getProduct(id));
        
        return "product_template";
    }

    @PostMapping("/{id}/add-review")
    public String submitReview(Model model, @PathVariable Long id, @ModelAttribute Review review, HttpServletRequest request) {
        User user = userService.getLoggedUser(request);
        if (user == null) return "redirect:/login";

        Product product = productService.getProduct(id);
        review.setOwner(user);
        review.setProduct(product);

        // Set the id to null to force to create new review
        review.setId(null);
        reviewService.save(review);
        return "redirect:/product/" + id; 
    }

    @PostMapping("/{productId}/review/{reviewId}/delete")
    public String deleteReview(@PathVariable Long productId, @PathVariable Long reviewId) {
        reviewService.delete(reviewId);
        return "redirect:/product/" + productId;
    }

    @PostMapping("/{id}/add-to-cart")
    public String addProductToCart(Model model, @PathVariable Long id, HttpServletRequest request){
        User user = userService.getLoggedUser(request);
        if (user == null) return "redirect:/login";

        user.addProductToCart(productService.getProduct(id));
        userService.save(user);
        return "redirect:/cart";
    }

    @GetMapping("/search")
    public String getSearch(Model model, @RequestParam(required=false) String name, @RequestParam(required=false) String productType) {
        if (name != null) {
            // If it comes from home page loads query for name search
            model.addAttribute("name", name);
            model.addAttribute("productList", productService.findByNameContaining(name));
            model.addAttribute("productTypes", productService.getAllProductTypesAndCount());
        } else if (productType != null) {
            // If it comes from nav bar for product type search
            ProductType productTypeObj = ProductType.valueOf(productType);
            model.addAttribute("productList", productService.getProductsByProductType(productTypeObj));
            model.addAttribute("productTypes", productService.getAllProductTypesAndCount(productTypeObj));
        } else {
            // Otherwise loads all products
            model.addAttribute("productList", productService.getAllProducts());
            model.addAttribute("productTypes", productService.getAllProductTypesAndCount());
        }

        model.addAttribute("tags", productService.getAllTags());
        // model.addAttribute("user", userComponent.getUser());

		return "search_template";
    }

    @PostMapping("/search")
    public String searchProducts(
            Model model, 
            @RequestParam(required=false) String name, 
            @RequestParam(required=false) Integer minPrice, 
            @RequestParam(required=false) Integer maxPrice,
            @RequestParam(required=false) List<String> tags,
            @RequestParam(required=false) List<String> productType
    ) {
        // Add attributes to the model
        model.addAttribute("productList", productService.searchProducts(name, minPrice, maxPrice, tags, productType));
        model.addAttribute("productTypes", productService.getAllProductTypesAndCount());
        model.addAttribute("tags", productService.getAllTags());

        return "search_template";
    }

    @GetMapping("/{id}/update")
    public String editProduct(Model model, @PathVariable Long id) {
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "product_update_template";
    }

    @PostMapping ("/{id}/update")
    @Transactional
    public String updateProduct (
            Model model,
            @PathVariable Long id,
            @RequestParam List<MultipartFile> imagesVal,
            @RequestParam String tagsVal,
            @Valid @ModelAttribute Product newProduct,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {
        if (bindingResult.hasErrors()) {
            // In case of errors, return to the form with the errors mapped
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("tagsVal", tagsVal);
            return "product_update_template";
        }

        Product oldProduct = productService.getProduct(id);

        List<ProductTag> oldTags = new ArrayList<>(oldProduct.getTags());

        // Sets the invariant fields
        newProduct.setId(id);

        User user = userService.getLoggedUser(request);
        if (user == null) return "redirect:/login";

        newProduct.setSeller(user);
        newProduct.setTotalSells(oldProduct.getTotalSells());
        newProduct.setReviews(oldProduct.getReviews());

        
        
        try {
            if (imagesVal != null && imagesVal.size() > 1) {
                imageService.saveImagesInProduct(newProduct, imagesVal);
            } else {
                newProduct.clearImages();
                for (Image image : new ArrayList<>(oldProduct.getImages())) {
                    oldProduct.removeImage(image);
                    newProduct.addImage(image);
                }
            }
        } catch (IOException e) {
            // In case of errors in the images, return to the form with the errors mapped
            model.addAttribute("imageError", "Error uploading images");
            model.addAttribute("tagsVal", tagsVal);
            return "product_update_template";
        }
        Product savedProduct = productService.save(newProduct);
        savedProduct.getTags().clear();
        List<ProductTag> newTags = productService.transformStringToTags(tagsVal);
        for (ProductTag tag : oldTags) {
            if (!newTags.contains(tag)) {
                tag.removeProduct(oldProduct);
                productService.saveTag(tag);
            }
        }
    
        for (ProductTag tag : newTags) {
            savedProduct.addTag(tag);
            if (!oldTags.contains(tag)) {
                tag.addProduct(savedProduct);
                productService.saveTag(tag);
            }
        }

        productService.save(savedProduct);
        //newProduct.setId(id);
        // productService.updateProduct(id, newProduct);
        return "redirect:/product/" + id;
    }

    @GetMapping("/new")
    public String ProductForm(Model model) {
        model.addAttribute("productTypes", ProductType.getMapped());
        return "product_create_template";
    }

    @PostMapping ("/new")
    public String newProduct (
            Model model,
            @RequestParam List<MultipartFile> imagesVal,
            @RequestParam String tagsVal,
            @Valid @ModelAttribute Product newProduct,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {
        if (bindingResult.hasErrors()) {
            // In case of errors, return to the form with the errors mapped
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("tagsVal", tagsVal);
            return "product_create_template";
        }

        User user = userService.getLoggedUser(request);
        if (user == null) return "redirect:/login";

        newProduct.setSeller(user);
        newProduct.setTags(productService.transformStringToTags(tagsVal));
        try {
            if (imagesVal != null && imagesVal.size() > 1) {
                imageService.saveImagesInProduct(newProduct, imagesVal);
            }
        } catch (IOException e) {
            // In case of errors in the images, return to the form with the errors mapped
            model.addAttribute("imageError", "Error uploading images");
            model.addAttribute("tagsVal", tagsVal);
            return "product_create_template";
        }

        List<ProductTag> tags = newProduct.getTags();
        for (ProductTag tag : tags) {
            tag.addProduct(newProduct);
        }
        Product savedProduct = productService.save(newProduct);
        user.addProductForSale(savedProduct);
        userService.save(user);
        return "redirect:/product/" + savedProduct.getId();
    }

    @GetMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }
}