package es.dlj.onlinestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.service.UserComponent;

@Controller
public class UserProfileController {

    @Autowired
    private UserComponent userComponent;
    
    @GetMapping("/userprofile")
    public String getUserProfile(Model model) {
        
        UserInfo user = userComponent.getUser();
        model.addAttribute("userInfo", user);

		return "userprofile_template";
    }

    @GetMapping("/editprofile")
    public String getEditProfile(Model model) {
        UserInfo user = userComponent.getUser();
        model.addAttribute("userInfo", user);

        return "editprofile_template";
    }


    @PostMapping("/save-editprofilechanges")
    public String saveProfileChanges(Model model, 
                                 @RequestParam String userName, 
                                 @RequestParam String userSurname, 
                                 @RequestParam String userEmail, 
                                 @RequestParam int userPhone, 
                                 @RequestParam String userAddress, 
                                 @RequestParam String userCity, 
                                 @RequestParam String userPostalCode) {

    UserInfo user = userComponent.getUser();
    
    user.setName(userName);
    user.setSurname(userSurname);
    user.setEmail(userEmail);
    user.setPhone(userPhone);
    user.setAddress(userAddress);
    user.setCity(userCity);
    user.setPostalCode(userPostalCode);

    userComponent.setUser(user.getId());

    return "redirect:/userprofile"; 
    }

}