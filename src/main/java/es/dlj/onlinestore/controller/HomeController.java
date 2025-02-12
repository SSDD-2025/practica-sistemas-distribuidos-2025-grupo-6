package es.dlj.onlinestore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/home")
    public String getHome(Model model) {

        model.addAttribute("userName", "World");

		return "home_template";
    }

}
