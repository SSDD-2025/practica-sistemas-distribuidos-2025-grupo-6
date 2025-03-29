package es.dlj.onlinestore.controller.web;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.dto.UserFormDTO;
import es.dlj.onlinestore.dto.UserSimpleDTO;
import es.dlj.onlinestore.mapper.UserMapper;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserMapper userMapper;

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

    @GetMapping("/")
    public String getHome(Model model) {
        // Define a record to hold the information of a section
        record Section(String title, String color, String icon, List<Product> products) {}

        // Define the sections to show at the Home page
        List<Section> sections = List.of(
                new Section("Best Sellers", "primary", "award", productService.getBestSellers()),
                new Section("On Sale", "success", "tag", productService.getBestSales()),
                new Section("Low Stock", "warning", "exclamation-triangle", productService.getLowStock(10))
        );

        // Add atributtes to the model
        model.addAttribute("sections", sections);
        model.addAttribute("tags", productService.getAllTags());
        model.addAttribute("productTypes", productService.getAllProductTypesAndCount());

        return "home_template";
    }

    @GetMapping("/privacy")
    public String getPrivacy() {
        return "privacy_template";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login_template";
    }

    @GetMapping("/login-error")
    public String getLoginError(Model model) {
        model.addAttribute("error", true);
        return "login_template";
    }
    
    @GetMapping("/register")
    public String getRegister() {
        return "register_template";
    }

    @PostMapping("/register")
    public String registerUser(
            Model model, 
            @Valid @ModelAttribute UserFormDTO newUser, 
            BindingResult bindingResult, 
            @RequestParam MultipartFile image
    ) {

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            // In case of errors, return to the form with the errors mapped
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("user", newUser);
            return "register_template";
        }

        if (!newUser.password().equals(newUser.repeatedPassword())) {
            model.addAttribute("confirmPasswordError", "Passwords do not match");
            model.addAttribute("user", newUser);
            return "register_template";
        }

        // Check if the user already exists
        if (userService.findByUserDTOName(newUser.username()).isPresent()) {
            model.addAttribute("usernameError", "User already exists, try to login instead");
            model.addAttribute("user", newUser);
            return "register_template";
        }

        UserDTO updatedUser = userService.saveUserDTO(userMapper.toDTO(newUser));

        try {
            if (image != null && !image.isEmpty()) {
                imageService.saveImageInUser(updatedUser, image);
            }
        } catch (IOException e) {
            // In case of errors in the images, return to the form with the errors mapped
            model.addAttribute("imageError", "Error uploading profile image");
            model.addAttribute("user", newUser);
            return "register_template";
        }

        // Save the new user
        userService.saveUserDTO(updatedUser);
        return "redirect:/login";
    }
    
}
