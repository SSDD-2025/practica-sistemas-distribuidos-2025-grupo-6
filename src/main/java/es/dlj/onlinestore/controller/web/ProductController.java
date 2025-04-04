package es.dlj.onlinestore.controller.web;

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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page; 

import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ReviewDTO;
import es.dlj.onlinestore.enumeration.ProductType;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.ReviewService;
import es.dlj.onlinestore.service.UserService;
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

    @GetMapping("/{id}")
    public String loadProductDetails(Model model, @PathVariable Long id) {
        model.addAttribute("product", productService.findDTOById(id));
        model.addAttribute("isOwnerProduct", productService.isProductOwner(id));
        return "product_template";
    }

    @PostMapping("/{id}/add-review")
    public String submitReview(Model model, @PathVariable Long id, @ModelAttribute ReviewDTO reviewDTO) {
        reviewService.save(id, reviewDTO);
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
        return "redirect:/cart";
    }

    @GetMapping("/search")
    public String getSearch(
            Model model, 
            @RequestParam(required=false) String name, 
            @RequestParam(required=false) String productType,
            @PageableDefault(size = 8, page = 0) Pageable pageable
    ) {
        Page<ProductDTO> productPage; 
        if (name != null) {
            // If it comes from home page loads query for name search
            model.addAttribute("name", name);
            productPage = productService.findAllDTOsByNameContaining(name, pageable);  
            model.addAttribute("productTypes", productService.getAllProductTypesAndCount());
        } else if (productType != null) {
            // If it comes from nav bar for product type search
            ProductType productTypeObj = ProductType.valueOf(productType);
            productPage = productService.findAllDTOsByProductType(productTypeObj, pageable); 
            model.addAttribute("productTypes", productService.getAllProductTypesAndCount(productTypeObj));
        } else {
            // Otherwise loads all products
            productPage = productService.findAllDTOs(pageable);
            model.addAttribute("productTypes", productService.getAllProductTypesAndCount());
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("prevPage", productPage.getNumber() > 0 ? productPage.getNumber() - 1 : 0);
        model.addAttribute("nextPage", productPage.getNumber() < productPage.getTotalPages() - 1 ? productPage.getNumber() + 1 : productPage.getNumber());
        model.addAttribute("hasMultiplePages", productPage.getTotalPages() > 1);

        List<Map<String, Object>> pages = new ArrayList<>();
        for (int i = 0; i < productPage.getTotalPages(); i++) {
            Map<String, Object> pag = new HashMap<>();
            pag.put("number", i);
            pag.put("display", i + 1);
            pag.put("current", i == productPage.getNumber());
            pages.add(pag);
        }
        model.addAttribute("pages", pages);

        model.addAttribute("tags", productService.getAllTags());
        model.addAttribute("currentName", name);
        model.addAttribute("currentProductType", productType);
        model.addAttribute("currentSize", pageable.getPageSize());

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
        model.addAttribute("productList", productService.findAllDTOsBy(name, minPrice, maxPrice, tags, productType));
        model.addAttribute("productTypes", productService.getAllProductTypesAndCount());
        model.addAttribute("tags", productService.getAllTags());
        return "search_template";
    }

    @GetMapping("/{id}/update")
    public String editProduct(Model model, @PathVariable Long id) {
        // Check if the user is the owner of the product or administrator
        if (!productService.isProductOwner(id)) return "redirect:/product/" + id;

        model.addAttribute("product", productService.findDTOById(id));
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
        // Check if the user is the owner of the product or administrator
        if (!productService.isProductOwner(id)) return "redirect:/product/" + id;
        
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
        
        if (bindingResult.hasErrors()) {
            // In case of errors, return to the form with the errors mapped
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("productTypes", ProductType.getMapped());
            model.addAttribute("errors", errors);
            model.addAttribute("tagsVal", tagsVal);
            return "product_create_template";
        }
        
        ProductDTO savedProductDTO = productService.saveProduct(newProductDTO, imagesVal, tagsVal); 
        return "redirect:/product/" + savedProductDTO.id();
    }

    @GetMapping("/{id}/delete")
    public String deleteProduct(Model model, @PathVariable Long id) {
        if (productService.isProductOwner(id)){
            productService.deleteProduct(id);
        }
        return "redirect:/";
    }
    
}