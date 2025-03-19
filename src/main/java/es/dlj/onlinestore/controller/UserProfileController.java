package es.dlj.onlinestore.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.dlj.onlinestore.model.Image;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.OrderService;
import es.dlj.onlinestore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            UserInfo user = userService.findByUserName(principal.getName()).get();
            model.addAttribute("user", user);
            model.addAttribute("isLogged", true);
            model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
            model.addAttribute("isUser", request.isUserInRole("USER"));

        } else {
            model.addAttribute("isLogged", false);
        }
    }

    @GetMapping
    public String getUserProfile(Model model, HttpServletRequest request) {
        // model.addAttribute("user", userComponent.getUser());
        return "profile_template";
    }

    @GetMapping("/update")
    public String getEditProfile(Model model) {
        // model.addAttribute("user", userComponent.getUser());
        return "profile_update_template";
    }

    @PostMapping("/update")
    public String saveProfileChanges(
            Model model, 
            @Valid @ModelAttribute UserInfo newUser, 
            BindingResult bindingResult,
            @RequestParam(required=false) MultipartFile profilePhotoFile, 
            HttpServletRequest request
    ) {

        if (bindingResult.hasErrors()) {
            // In case of errors, return to the form with the errors mapped
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            // model.addAttribute("user", userComponent.getUser());
            return "profile_update_template";
        }

        UserInfo user = userService.getLoggedUser(request);
        if (user == null) return "redirect:/login";

        user.updateWith(newUser);
        if (profilePhotoFile != null) {
            try {
                if (!profilePhotoFile.isEmpty()) {
                    Image oldPhoto = user.getProfilePhoto();
                    Image image = imageService.saveFileImage(profilePhotoFile);
                    user.setProfilePhoto(image);
                    if (oldPhoto != null) {
                        imageService.deleteImage(oldPhoto);
                    }
                }
            } catch (IOException e) {
                // In case of errors in the images, return to the form with the errors mapped
                model.addAttribute("imageError", "Error uploading image");
                // model.addAttribute("user", userComponent.getUser());
                return "profile_update_template";
            }
        }
        userService.save(user);
        return "redirect:/profile";
    }

    @GetMapping("/order/{id}")
    public String getOrderView(Model model, @PathVariable Long id) {
        // model.addAttribute("user", userComponent.getUser());
        model.addAttribute("order", orderService.getReferenceById(id));
        return "order_template";
    }

    @PostMapping("/order/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return "redirect:/profile";
    }
}
