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


@Controller
public class LoginController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private UserService userService;

    @PostMapping("/login-access")
    public String getLogin(Model model, @RequestParam String userName, @RequestParam String password) {
        UserInfo user = userService.findByUserNameAndPassword(userName, password);
        if (user != null) {
            userComponent.setUser(user.getId());
            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        return "login_template";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        return "register_template";
    }

    @PostMapping("/register-access")
    public String getRegister(Model model, @RequestParam String userName, @RequestParam String password, @RequestParam String name, @RequestParam String lastName, @RequestParam String email) {
        UserInfo user = new UserInfo(userName, password, name, lastName, email, UserInfo.Role.USER);
        userService.save(user);
        userComponent.setUser(user.getId());
        return "redirect:/";
    }

}
