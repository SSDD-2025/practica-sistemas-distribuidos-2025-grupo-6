package es.dlj.onlinestore.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserComponent;

@Controller
public class HomeController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String getHome2(Model model) {

        List<Map<String, Object>> sections = new ArrayList<>();

        sections.add(Map.of("title", "Best Sellers", "products", productService.getBestSellers()));
        sections.add(Map.of("title", "Top Rated", "products", productService.getTopRated()));
        sections.add(Map.of("title", "On Sale", "products", productService.getOnSale(20)));
        sections.add(Map.of("title", "Trending This Week", "products", productService.getTrendingThisWeek()));
        sections.add(Map.of("title", "Low Stock", "products", productService.getLowStock(10)));
        
        model.addAttribute("sections", sections);
        model.addAttribute("tags", productService.getAllTags());
        model.addAttribute("SearchInOtherPage", true);

		return "home_template";
    }

    @GetMapping("/privacy")
    public String getPrivacy() {
        return "privacy_policy_template";
    }

}
