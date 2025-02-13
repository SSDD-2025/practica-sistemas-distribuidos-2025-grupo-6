/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package es.dlj.onlinestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.dlj.onlinestore.service.ProductService;

@Controller
class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public String loadProductDetails(Model model, @PathVariable Long id){

        model.addAttribute("product", productService.getProduct(id));

        return "product_template";
    }

}
