package es.dlj.onlinestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.service.UserComponent;

@Controller
public class UserProfileController {

    @Autowired
    private UserComponent userComponent;
    
    @GetMapping("/userprofile")
    public String getUserProfile(Model model) {

        if (!userComponent.isLoggedUser()) {
            return "redirect:/login"; 
        }
        
        UserInfo user = userComponent.getUser();

        model.addAttribute("userInfo", user);

		return "userprofile_template";
    }

}