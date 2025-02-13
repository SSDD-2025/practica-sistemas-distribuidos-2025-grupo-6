package es.dlj.onlinestore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String getHome(Model model) {

        model.addAttribute("userName", "World");
        model.addAttribute("productList", 0);

		return "home_template";
    }

}
