/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package es.dlj.onlinestore.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.ProductTag;
import es.dlj.onlinestore.model.ProductType;
import es.dlj.onlinestore.service.ProductService;


@Controller
class ProductController {

    //@Autowired
    //private UserComponent userComponent;

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public String loadProductDetails(Model model, @PathVariable Long id){

        model.addAttribute("product", productService.getProduct(id));

        return "productDetailed_template";
    }

    @GetMapping("/search")
    public String getHome1(Model model, @RequestParam(required=false) String name) {

        // If it comes from home page loads query for name search and returns the results
        if (name != null) {
            model.addAttribute("name", name);
            return this.searchProducts(model, name, null, null, null, null, 
            null, null, null, null, null, null, null, null, 
            null, null, null, null);
        }

        model.addAttribute("productList", productService.getAllProducts());
        model.addAttribute("tags", productService.getAllTags());

		return "search_template";

    }

    @PostMapping("/search")
    public String searchProducts(
        Model model, 
        @RequestParam(required=false) String name, 
        @RequestParam(required=false) Integer minPrice, 
        @RequestParam(required=false) Integer maxPrice,
        @RequestParam(required=false) List<String> tags,
        @RequestParam(required=false) String productType,
        @RequestParam(required=false) String minSale,
        @RequestParam(required=false) String maxSale,
        @RequestParam(required=false) String minRating,
        @RequestParam(required=false) String maxRating,
        @RequestParam(required=false) String minStock,
        @RequestParam(required=false) String maxStock,
        @RequestParam(required=false) String minWeekSells,
        @RequestParam(required=false) String maxWeekSells,
        @RequestParam(required=false) String minNumberRatings,
        @RequestParam(required=false) String maxNumberRatings,
        @RequestParam(required=false) String minTotalSells,
        @RequestParam(required=false) String maxTotalSells
    ) {

        List<Product> products = productService.searchProducts(name, minPrice, maxPrice, tags, productType, minSale, maxSale, minRating, maxRating, minStock, maxStock, minWeekSells, maxWeekSells, minNumberRatings, maxNumberRatings, minTotalSells, maxTotalSells);

        model.addAttribute("productList", products);
        model.addAttribute("tags", productService.getAllTags());

        
        
        return "search_template";

    }

    @PostMapping ("/product/update/{id}")
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
        Product product = productService.editProduct(id, name, price, sale, description, productType, stock, tagList);
        model.addAttribute("product", product);
        return "productDetailed_template";
    }

    @PostMapping ("/product/new")
    public String newProduct (
        Model model,
    @PathVariable Long id,
    @RequestParam String name,
    @RequestParam float price,
    @RequestParam String description,
    @RequestParam int stock,
    @RequestParam String tags,
    @RequestParam ProductType productType){
        List<String> tagList = Arrays.asList(tags.split("\\s*,\\s*"));
        productService.saveProduct(name, price, description, productType, stock, tagList);
        return "home_template";
    }
}
