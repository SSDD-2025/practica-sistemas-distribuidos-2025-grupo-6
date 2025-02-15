/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package es.dlj.onlinestore.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.ProductType;
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

        return "productDetailed_template";
    }

    @GetMapping("/search")
    public String searchProducts(
        Model model, 
        @RequestParam(required=false) String query, 
        @RequestParam(required=false) Integer minPrice, 
        @RequestParam(required=false) Integer maxPrice,
        @RequestParam(required=false) List<String> tags,
        @RequestParam(required=false) String productType
    ) {
        
        Product productProbe = new Product();

        if (query != null && !query.isEmpty()) {
            productProbe.setName(query);
            model.addAttribute("query", query);
        }
        if (tags != null && !tags.isEmpty()) {
            productProbe.setTags(new ArrayList<>(tags));
            model.addAttribute("tags", tags);
        }
        if (productType != null) {
            productProbe.setProductType(ProductType.fromString(productType));
            model.addAttribute("productType", productType);
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("id", "price", "stock", "rating", "numberRatings", "totalSells", "lastWeekSells", "sale");

        if (minPrice != null || maxPrice != null) {
            matcher = matcher.withTransformer("price", value -> Optional.of(value.filter(v -> {
                Integer price = (Integer) v;
                return (minPrice == null || price >= minPrice) && (maxPrice == null || price <= maxPrice);
            })));
            model.addAttribute("minPrice", minPrice);
            model.addAttribute("maxPrice", maxPrice);
        } else {
            model.addAttribute("minPrice", 0);
            model.addAttribute("maxPrice", 1000);
        }

        Example<Product> example = Example.of(productProbe, matcher);
        List<Product> products = productService.findAll(example);

        model.addAttribute("productList", products);
        return "home_template";

    }
}
