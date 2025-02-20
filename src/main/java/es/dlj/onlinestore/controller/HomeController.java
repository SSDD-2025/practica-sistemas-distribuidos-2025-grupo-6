package es.dlj.onlinestore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserComponent;

@Controller
public class HomeController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String getHome(Model model) {

        record Section(String title, String color, String icon, List<Product> products) {}

        List<Section> sections = List.of(
            new Section("Best Sellers", "primary", "award", productService.getBestSellers()),
            new Section("Top Rated", "warning", "star", productService.getTopRated()),
            new Section("On Sale", "success", "tag", productService.getOnSale(20)),
            new Section("Trending This Week", "danger", "fire", productService.getTrendingThisWeek()),
            new Section("Low Stock", "warning", "exclamation-triangle", productService.getLowStock(10))
        );
        
        model.addAttribute("user", userComponent.getUser());
        model.addAttribute("sections", sections);
        model.addAttribute("tags", productService.getAllTags());
        model.addAttribute("productTypes", productService.getAllProductTypesAndCount());
        model.addAttribute("SearchInOtherPage", true);

        return "home_template";
    }

    @GetMapping("/privacy")
    public String getPrivacy() {
        return "privacy_policy_template";
    }
    
}
