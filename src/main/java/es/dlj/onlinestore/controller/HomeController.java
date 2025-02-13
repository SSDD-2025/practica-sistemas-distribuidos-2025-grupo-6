package es.dlj.onlinestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.dlj.onlinestore.model.UserInfo;
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

        UserInfo user = userComponent.getUser();

        model.addAttribute("userName", user.getUserName());
        model.addAttribute("productList", productService.getAllProducts());

		return "home_template";
    }

    @GetMapping("/product/{id}")
    public String getProduct(Model model, @PathVariable long id) {

        model.addAttribute("product", productService.getProduct(id));

        return "product_template";
    }

}
