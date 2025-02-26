package es.dlj.onlinestore.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

import es.dlj.onlinestore.enumeration.ProductType;
import es.dlj.onlinestore.model.Image;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.Review;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserComponent;
import es.dlj.onlinestore.service.UserReviewService;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/product")
class ProductController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserReviewService reviewService;

    @GetMapping("/{id}")
    public String loadProductDetails(Model model, @PathVariable Long id){
        Product product = productService.getProduct(id);
        List<Review> reviews = reviewService.getReviewsByProduct(product);
        int numberOfReviews = reviews.size();
        product.setNumberRatings(numberOfReviews);

        List<Map<String, Object>> images = new ArrayList<>();
        List<Image> productImages = product.getImages();
        for (int i = 0; i < productImages.size(); i++) {
            images.add(Map.of("id", productImages.get(i).getId(), "isMainImage", (i == 0)));
        }

        model.addAttribute("allImages", images);
        model.addAttribute("reviews", reviews);
        model.addAttribute("user", userComponent.getUser());
        model.addAttribute("product", productService.getProduct(id));
        
        return "productDetailed_template";
    }

    @PostMapping("/{id}/add-to-cart")
    public String addProductToCart(Model model, @PathVariable Long id){
        userComponent.addProductToCart(productService.getProduct(id));
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
        model.addAttribute("user", userComponent.getUser());

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

    @GetMapping("/update/{id}")
    public String editProduct(Model model, @PathVariable Long id) {
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "productEdit_template";
    }

    @PostMapping ("/update/{id}")
    public String updateProduct (
        Model model,
        @PathVariable Long id,
        @RequestParam List<MultipartFile> imagesVal,
        @RequestParam String tagsVal,
        @Valid @ModelAttribute Product newProduct,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            // In case of errors, return to the form with the errors mapped
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("tagsVal", tagsVal);
            return "productEdit_template";
        }

        //newProduct.setSeller(userComponent.getUser());
        newProduct.setTags(productService.transformStringToTags(tagsVal));
        try {
            imageService.saveImagesInProduct(productService.getProduct(id), imagesVal);
        } catch (IOException e) {
            // In case of errors in the images, return to the form with the errors mapped
            model.addAttribute("imageError", "Error uploading images");
            model.addAttribute("tagsVal", tagsVal);
            return "productEdit_template";
        }

        productService.updateProduct(id, newProduct);
        return "redirect:/product/" + id;
    }

    @GetMapping("/new")
    public String ProductForm(Model model) {
        model.addAttribute("productTypes", ProductType.getMapped());
        return "productForm_template";
    }

    @PostMapping ("/new")
    public String newProduct (
        Model model,
        @RequestParam List<MultipartFile> imagesVal,
        @RequestParam String tagsVal,
        @Valid @ModelAttribute Product newProduct,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            // In case of errors, return to the form with the errors mapped
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("tagsVal", tagsVal);
            return "productForm_template";
        }

        newProduct.setSeller(userComponent.getUser());
        newProduct.setTags(productService.transformStringToTags(tagsVal));
        try {
            imageService.saveImagesInProduct(newProduct, imagesVal);
        } catch (IOException e) {
            // In case of errors in the images, return to the form with the errors mapped
            model.addAttribute("imageError", "Error uploading images");
            model.addAttribute("tagsVal", tagsVal);
            return "productForm_template";
        }

        Product savedProduct = productService.saveProduct(newProduct);
        userComponent.addProductForSale(savedProduct);
        return "redirect:/product/" + savedProduct.getId();
    }
}