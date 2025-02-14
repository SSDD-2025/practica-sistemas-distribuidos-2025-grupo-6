package es.dlj.onlinestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserComponent;

@Controller
public class UserProfileController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private ProductService productService;
    
    @GetMapping("/userprofile")
    public String getUserProfile(Model model) {

        if (!userComponent.isLoggedUser()) {
            return "redirect:/login"; 
        }
        
        UserInfo user = userComponent.getUser();
        
        
        model.addAttribute("userName", user.getName());
        model.addAttribute("userSurname", user.getSurname());
        model.addAttribute("userEmail", user.getEmail()); 
        model.addAttribute("userPhone", user.getPhone());
        model.addAttribute("userAddress", user.getAddress());
        model.addAttribute("userCity", user.getCity());
        model.addAttribute("userPostalCode", user.getPostalCode());
        model.addAttribute("userProfilePhoto", user.getProfilePhoto());
        model.addAttribute("userCreditCart", user.getCreditCard()); 



		return "userprofile_template";
    }

}