package es.dlj.onlinestore.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.enumeration.PaymentMethod;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.service.UserComponent;
import es.dlj.onlinestore.service.UserService;

@Controller
public class UserProfileController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private UserService userService;
    
    @GetMapping("/userprofile")
    public String getUserProfile(Model model) {
        
        UserInfo user = userComponent.getUser();
        model.addAttribute("user", user);

		return "userprofile_template";
    }

    @GetMapping("/editprofile")
    public String getEditProfile(Model model) {
        UserInfo user = userComponent.getUser();
        
        List<Map<String, Object>> paymentMethods = new ArrayList<>();
        for (PaymentMethod pMethod : PaymentMethod.values()) {
            paymentMethods.add(Map.of("name", pMethod.getName(), 
                                      "selected", (user.getPaymentMethod() != null && user.getPaymentMethod().equals(pMethod))));
        }

        model.addAttribute("paymentMethods", paymentMethods);
        model.addAttribute("user", user);

        return "editprofile_template";
    }


    @PostMapping("/save-editprofilechanges")
    public String saveProfileChanges(Model model, 
                                 @RequestParam String name, 
                                 @RequestParam String surname, 
                                 @RequestParam String email, 
                                 @RequestParam String phone, 
                                 @RequestParam String address, 
                                 @RequestParam String city, 
                                 @RequestParam String postalCode,
                                 @RequestParam String paymentMethod) {

    UserInfo user = userComponent.getUser();
    
    user.setName(name);
    user.setSurname(surname);
    user.setEmail(email);
    user.setPhone(phone);
    user.setAddress(address);
    user.setCity(city);
    user.setPostalCode(postalCode);
    user.setPaymentMethod(PaymentMethod.fromString(paymentMethod));
    userService.save(user);
    userComponent.setUser(user.getId());

    return "redirect:/userprofile"; 
    }

}