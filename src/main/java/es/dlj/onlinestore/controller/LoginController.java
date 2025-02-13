package es.dlj.onlinestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.repository.UserInfoRepository;


@Controller
public class LoginController {

    @Autowired
    private UserInfoRepository users;

    @PostMapping("/login-access")
    public String getLogin(Model model, @RequestParam String userName, @RequestParam String password) {
        if (users.existsByUserNameAndPassword(userName, password)) {
            model.addAttribute("userName", userName);
            return "home_template";
        } else {
            model.addAttribute("userName", "");
            return "login_template";
        }
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("userName", "");
        return "login_template";
    }

}
