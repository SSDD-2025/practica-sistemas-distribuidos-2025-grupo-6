package es.dlj.onlinestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.service.UserComponent;
import es.dlj.onlinestore.service.UserService;

/**
 * Temporal controller to manage the access to the application (without security)
 */
@Controller
public class AccessController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private UserService userService;

    @PostMapping("/login-access")
    public String getLogin(Model model, @RequestParam String userName, @RequestParam String password) {
        UserInfo user = userService.findByUserNameAndPassword(userName, password);
        if (user != null) {
            userComponent.setUser(user.getId());
            return "redirect:/userprofile";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("user", userComponent.getUser());
        return "login_template";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("user", userComponent.getUser());
        return "register_template";
    }

    @PostMapping("/register-access")
    public String getRegister(Model model, @RequestParam String userName, @RequestParam String password, @RequestParam String name, @RequestParam String lastName, @RequestParam String email, @RequestParam String address, @RequestParam String city, @RequestParam String postalCode, @RequestParam String phone) {
        UserInfo user = new UserInfo(userName, password, name, lastName, email, UserInfo.Role.USER, address, city, postalCode, phone);
        userService.save(user);
        userComponent.setUser(user.getId());
        return "redirect:/userprofile";
    }

}
