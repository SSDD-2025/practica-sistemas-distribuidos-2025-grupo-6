package es.dlj.onlinestore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserComponent;
import es.dlj.onlinestore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

            UserInfo user = userService.findByUserName(principal.getName()).get();

            model.addAttribute("logged", true);
            model.addAttribute("userName", user.getUserName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));
            model.addAttribute("user", user);
        } else {
            model.addAttribute("logged", false);
        }
    }

    @GetMapping("/")
    public String getHome(Model model) {
        // Define a record to hold the information of a section
        record Section(String title, String color, String icon, List<Product> products) {

        }

        // Define the sections to show at the Home page
        List<Section> sections = List.of(
                new Section("Best Sellers", "primary", "award", productService.getBestSellers()),
                new Section("On Sale", "success", "tag", productService.getBestSales()),
                new Section("Low Stock", "warning", "exclamation-triangle", productService.getLowStock(10))
        );

        // Add atributtes to the model
        model.addAttribute("user", userComponent.getUser());
        model.addAttribute("sections", sections);
        model.addAttribute("tags", productService.getAllTags());
        model.addAttribute("productTypes", productService.getAllProductTypesAndCount());

        return "home_template";
    }

    @GetMapping("/privacy")
    public String getPrivacy() {
        return "privacy_template";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login_template";
    }

    // @PostMapping("/login")
    // public String postLogin() {

    //     return "redirect:/profile";
    // }
    
    @GetMapping("/register")
    public String getRegister() {
        return "register_template";
    }
}
