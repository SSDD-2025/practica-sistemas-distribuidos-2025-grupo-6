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

        UserInfo user = userComponent.getUser();
/*
        model.addAttribute("userName", "Lídia");
        model.addAttribute("userSurname", "Budiós");
        model.addAttribute("userEmail", "lidia@gmail.com"); 
        model.addAttribute("userPhone", "678354091");
        model.addAttribute("userAddress", "Av. Universidad, 7");
        model.addAttribute("userCity", "Leganes");
        model.addAttribute("userPostalCode", "28911");
        model.addAttribute("userProfilePhoto", "https://via.placeholder.com/150"); // O cualquier URL de imagen
        model.addAttribute("userCreditCard", "hsdisoi");
        */
   
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