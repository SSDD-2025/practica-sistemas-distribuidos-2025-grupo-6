package es.dlj.onlinestore.controller;

import java.io.IOException;
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
import es.dlj.onlinestore.repository.OrderRepository;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.UserComponent;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ImageService imageService;

    @GetMapping
    public String getUserProfile(Model model) {
        model.addAttribute("user", userComponent.getUser());
        return "userprofile_template";
    }

    @GetMapping("/update")
    public String getEditProfile(Model model) {
        model.addAttribute("user", userComponent.getUser());
        return "editprofile_template";
    }

    @PostMapping("/update")
    public String saveProfileChanges(
            Model model, 
            @Valid @ModelAttribute UserInfo newUser, 
            BindingResult bindingResult,
            @RequestParam(required=false) MultipartFile profilePhotoFile
    ) {

        if (bindingResult.hasErrors()) {
            // In case of errors, return to the form with the errors mapped
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("user", userComponent.getUser());
            return "editprofile_template";
        }

        UserInfo user = userComponent.getUser();
        user.updateWith(newUser);
        if (profilePhotoFile != null) {
            try {
                Image oldPhoto = user.getProfilePhoto();
                Image image = imageService.saveFileImage(profilePhotoFile);
                userComponent.getUser().setProfilePhoto(image);
                if (oldPhoto != null) {
                    imageService.deleteImage(oldPhoto);
                }
            } catch (IOException e) {
                // TODO: inspired in product edit
            }
        }
        return "redirect:/profile";
    }

    @GetMapping("/order/{id}")
    public String getOrderView(Model model, @PathVariable Long id) {
        model.addAttribute("user", userComponent.getUser());
        model.addAttribute("order", orderRepository.getReferenceById(id));
        return "order_template";
    }

}
