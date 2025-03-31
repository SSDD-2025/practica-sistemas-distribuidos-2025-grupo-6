package es.dlj.onlinestore.controller.web;

import java.io.IOException;
import java.util.Collection;
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

import es.dlj.onlinestore.dto.ProductSimpleDTO;
import es.dlj.onlinestore.dto.UserFormDTO;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserService;
import jakarta.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/")
    public String getHome(Model model) {

        // Define a record to hold the information of a section
        record Section(String title, String color, String icon, Collection<ProductSimpleDTO> products) {}

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
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("user", newUser);
            return "register_template";
        }

        // Check if the password and repeated password match
        List<String> error = userService.checkNewUserError(newUser);
        if (error != null) {
            model.addAttribute(error.get(0), error.get(1));
            model.addAttribute("user", newUser);
            return "register_template";
        }

        userService.saveDTO(newUser);

        try {
            if (image != null && !image.isEmpty()) {
                imageService.saveImageInUser(image);
            }
        } catch (IOException e) {
            // In case of errors in the images, return to the form with the errors mapped
            model.addAttribute("imageError", "Error uploading profile image");
            model.addAttribute("user", newUser);
            return "register_template";
        }
        
        return "redirect:/login";
    }
    
}
