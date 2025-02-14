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
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserComponent;


@Controller
class ProductController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public String loadProductDetails(Model model, @PathVariable Long id){

        model.addAttribute("product", productService.getProduct(id));

        return "product_template";
    }

    @GetMapping("/search")
    public String searchProducts(Model model, @RequestParam String query, @RequestParam Integer minPrice, @RequestParam Integer maxPrice) {
        
        List<Product> products = productService.searchProducts(query, Float.valueOf(minPrice),  Float.valueOf(maxPrice));

        UserInfo user = userComponent.getUser();

        model.addAttribute("userName", user.getUserName());
        model.addAttribute("productList", products);
        model.addAttribute("query", query);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

		return "home_template";

    }
    

}
