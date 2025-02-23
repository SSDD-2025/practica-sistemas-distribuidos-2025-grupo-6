package es.dlj.onlinestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.OrderRepository;
import es.dlj.onlinestore.service.UserComponent;

@Controller
public class UserProfileController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/userprofile")
    public String getUserProfile(Model model) {
        model.addAttribute("user", userComponent.getUser());
        return "userprofile_template";
    }

    @GetMapping("/editprofile")
    public String getEditProfile(Model model) {
        model.addAttribute("user", userComponent.getUser());
        return "editprofile_template";
    }

    @PostMapping("/save-editprofilechanges")
    public String saveProfileChanges(Model model, @ModelAttribute UserInfo newUser) {
        userComponent.getUser().updateWith(newUser);
        return "redirect:/userprofile";
    }

    @GetMapping("/order/{id}")
    public String getOrderView(Model model, @PathVariable Long id) {
        model.addAttribute("user", userComponent.getUser());
        model.addAttribute("order", orderRepository.getReferenceById(id));
        return "order_template";
    }

}
