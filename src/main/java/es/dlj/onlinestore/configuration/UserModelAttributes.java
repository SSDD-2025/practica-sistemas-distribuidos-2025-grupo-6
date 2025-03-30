package es.dlj.onlinestore.configuration;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.dlj.onlinestore.dto.UserSimpleDTO;
import es.dlj.onlinestore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = "es.dlj.onlinestore.controller.web")
public class UserModelAttributes {

	@Autowired
	private UserService userService;
    
	@ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            UserSimpleDTO user = userService.findByUserSimpleDTOName(principal.getName());

            model.addAttribute("user", user);
            model.addAttribute("isLogged", true);
            model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
            model.addAttribute("isUser", request.isUserInRole("USER"));

        } else {
            model.addAttribute("isLogged", false);
        }
    }

}
