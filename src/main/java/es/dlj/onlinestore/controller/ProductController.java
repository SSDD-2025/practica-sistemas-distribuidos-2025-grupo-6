/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package es.dlj.onlinestore.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.dlj.onlinestore.enumeration.ProductType;
import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserComponent;


@Controller
@RequestMapping("/product")
class ProductController {

    private Logger log;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public String loadProductDetails(Model model, @PathVariable Long id){

        model.addAttribute("product", productService.getProduct(id));
        model.addAttribute("user", userComponent.getUser());

        return "productDetailed_template";
    }

    @PostMapping("/{id}/add-to-cart")
    public String addProductToCart(Model model, @PathVariable Long id){

        userComponent.addProductToCart(productService.getProduct(id));

        return "redirect:/cart";
    }


    @GetMapping("/search")
    public String getHome1(Model model, @RequestParam(required=false) String name, @RequestParam(required=false) String productType) {

        if (name != null) {
            // If it comes from home page loads query for name search and returns the results
            model.addAttribute("name", name);
            model.addAttribute("productList", productService.findByNameContaining(name));
            model.addAttribute("productTypes", productService.getAllProductTypesAndCount());

        } else if (productType != null) {
            // If it comes from nav bar for product type search and returns the results
            List<Map<String, Object>> productTypeMap = productService.getAllProductTypesAndCount();

            for (int i = 0; i < productTypeMap.size(); i++) {
                Map<String, Object> map = productTypeMap.get(i);
                if (map.get("name").equals(productType)) {
                    Map<String, Object> newMap = new HashMap<>(map);
                    newMap.put("selected", true);
                    productTypeMap.set(i, newMap);
                    break;
                }
            }
            model.addAttribute("productList", productService.getProductsByProductType(ProductType.fromString(productType)));
            model.addAttribute("productTypes", productTypeMap);

        } else {

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

        List<Product> products = productService.searchProducts(name, minPrice, maxPrice, tags, productType);

        model.addAttribute("productList", products);
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
    @RequestParam ProductType productType){
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
        @RequestBody Map<String,Object> rawProduct
    )
    {
        Product product = new Product();
        String errorMessage=productService.checkForErrors(rawProduct);
        if(errorMessage.isEmpty()){
            model.addAttribute("errorMessage", "Name required to create product");
            return "productForm_template";
        }
        else{
            ProductType productType = productService.transformStringtoProductType((String) rawProduct.get("productType"));
            product = productService.saveProduct((String)rawProduct.get("name"),(float) rawProduct.get("price"), (String) rawProduct.get("description"), productType, (int)rawProduct.get("stock"), (List<String>)rawProduct.get("tags"),(List<MultipartFile>)rawProduct.get("images"), (MultipartFile)rawProduct.get("mainImage"));
            return "home_template";
        }
        
    }

    @PostMapping("/form")
    public String ProductForm(Model model, @RequestParam long id) {
        if (id > 0) {
            Product product = productService.getProduct(id);
            log.info(product.toString());
            model.addAttribute("product", product);
        }
        return "productForm_template";  // Use a redirect to a GET endpoint that displays the product
    }

}
