package es.dlj.onlinestore.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.enumeration.ProductType;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.Review;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.ProductService.RawProduct;
import es.dlj.onlinestore.service.UserComponent;
import es.dlj.onlinestore.service.UserRatingService;


@Controller
@RequestMapping("/product")
class ProductController {

    private Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRatingService reviewService;

    @GetMapping("/{id}")
    public String loadProductDetails(Model model, @PathVariable Long id){
        List<Review> reviews = reviewService.getReviewsByProduct(productService.getProduct(id));
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
            ProductType productTypeObj = ProductType.fromString(productType);
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

    @PostMapping ("/update/{id}")
    public String updateProduct (
        Model model,
        @PathVariable Long id,
        @RequestParam String name,
        @RequestParam float price,
        @RequestParam String description,
        @RequestParam int stock,
        @RequestParam int sale,
        @RequestParam String tags,
        @RequestParam ProductType productType
    ){
        List<String> tagList = Arrays.asList(tags.split("\\s*,\\s*"));
        Product product = null;
        try {
            product = productService.editProduct(id, name, price, sale, description, productType, stock, tagList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("product", product);
        return "productDetailed_template";
    }

    @PostMapping ("/new")
    public String newProduct (
        Model model,
        @ModelAttribute RawProduct product
    )
    {
        String errorMessage = productService.checkForProductFormErrors(product);
        if (errorMessage.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
            return "productForm_template";
        }

        Product savedProduct = productService.saveProduct(product);
        return "redirect:/product/" + savedProduct.getId();
    }

    @GetMapping("/form")
    public String ProductForm(Model model) {
        return "productForm_template";
    }

    @PostMapping("/form")
    public String ProductForm(Model model, @RequestParam long id) {
        if (id > 0) {
            Product product = productService.getProduct(id);
            boolean newProduct = product.getProductType() == ProductType.NEW;
            boolean reconditionedProduct = product.getProductType() == ProductType.RECONDITIONED;
            boolean secondHandProduct = product.getProductType() == ProductType.SECONDHAND;
            model.addAttribute("new", newProduct);
            model.addAttribute("secondHand", secondHandProduct);
            model.addAttribute("reconditioned", reconditionedProduct);
            model.addAttribute("product", product);
        }
        return "productForm_template";
    }

}
