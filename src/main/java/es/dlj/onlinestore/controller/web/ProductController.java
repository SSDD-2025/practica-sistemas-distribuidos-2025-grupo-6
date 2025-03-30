package es.dlj.onlinestore.controller.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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

import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ReviewDTO;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.dto.UserSimpleDTO;
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
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            UserSimpleDTO user = userService.findByUserSimpleDTOName(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("isLogged", true);
            model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
            model.addAttribute("isUser", request.isUserInRole("USER"));
            
        } else {
            model.addAttribute("isLogged", false);
        }
    }

    @GetMapping("/{id}")
    public String loadProductDetails(Model model, @PathVariable Long id) {
        ProductDTO product = productService.getProduct(id);

        // Create a list of maps to store image names and stablish if has to be shown as first
        List<Map<String, Object>> images = new ArrayList<>();
        List<ImageDTO> productImages = product.images();
        for (int i = 0; i < productImages.size(); i++) {
            images.add(Map.of("name", productImages.get(i).id(), "selected", (i == 0)));
        }

        model.addAttribute("allImages", images);
        // model.addAttribute("user", userComponent.getUser());
        model.addAttribute("product", productService.getProduct(id));
        model.addAttribute("isOwnerProduct", productService.isOwnerProduct(id));
        
        return "product_template";
    }

    @PostMapping("/{id}/add-review")
    public String submitReview(Model model, @PathVariable Long id, @ModelAttribute ReviewDTO reviewDTO) {
        reviewService.saveReview(id, reviewDTO);
        return "redirect:/product/" + id; 
    }

    @PostMapping("/{productId}/review/{reviewId}/delete")
    public String deleteReview(@PathVariable Long productId, @PathVariable Long reviewId) {
        reviewService.delete(reviewId);
        return "redirect:/product/" + productId;
    }

    @PostMapping("/{id}/add-to-cart")
    public String addProductToCart(Model model, @PathVariable Long id){
        userService.addProductToCart(id);
        model.addAttribute("isOwnerProduct", productService.isOwnerProduct(id));
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
            model.addAttribute("productList", productService.getAllProductsDTO());
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
        ProductDTO product = productService.getProduct(id);
        
        model.addAttribute("product", product);
        model.addAttribute("isOwnerProduct", productService.isOwnerProduct(id));
        return "product_update_template";
    }

    @PostMapping ("/{id}/update")
    @Transactional
    public String updateProduct (
            Model model,
            @PathVariable Long id,
            @RequestParam List<MultipartFile> imagesVal,
            @RequestParam String tagsVal,
            @Valid @ModelAttribute ProductDTO newProduct,
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
            return "product_update_template";
        }

        ProductDTO product = productService.getProduct(id);
        
        model.addAttribute("isOwnerProduct", productService.isOwnerProduct(id));
        
        try {
            productService.updateProduct(id, newProduct, imagesVal, tagsVal); 

        } catch(NoSuchElementException e) {
            // In case of errors in the images, return to the form with the errors mapped
            model.addAttribute("imageError", "Error uploading images");
            model.addAttribute("tagsVal", tagsVal);
            return "product_update_template";
        }

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
            @Valid @ModelAttribute ProductDTO newProductDTO,
            BindingResult bindingResult
    ) {
        /* 
        if (bindingResult.hasErrors()) {
            // In case of errors, return to the form with the errors mapped
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("tagsVal", tagsVal);
            return "product_create_template";
        }*/

        UserDTO userDTO = userService.getLoggedUserDTO();
        
        ProductDTO savedProductDTO = productService.saveProduct(newProductDTO, imagesVal, tagsVal, userDTO); 
        return "redirect:/product/" + savedProductDTO.id();
    }

    @GetMapping("/{id}/delete")
    public String deleteProduct(Model model, @PathVariable Long id) {
        productService.deleteProduct(id);
        model.addAttribute("isOwnerProduct", productService.isOwnerProduct(id));
        return "redirect:/";
    }
    
}