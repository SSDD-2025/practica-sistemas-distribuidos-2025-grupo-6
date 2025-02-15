/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package es.dlj.onlinestore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.model.Product;
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
        return "search_template";

    }
}
