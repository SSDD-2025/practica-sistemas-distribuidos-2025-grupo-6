package es.dlj.onlinestore.controller.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.OrderDTO;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ReviewDTO;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.service.ImageService;
import es.dlj.onlinestore.service.OrderService;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.ReviewService;
import es.dlj.onlinestore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;
    
    @GetMapping
    public String getUserProfile(
            Model model, 
            HttpServletRequest request,
            @RequestParam(name = "orderPage", defaultValue = "0") int orderPageNum,
            @RequestParam(name = "reviewPage", defaultValue = "0") int reviewPageNum,
            @RequestParam(name = "productPage", defaultValue = "0") int productPageNum,
            @RequestParam(name = "orderSize", defaultValue = "4") int orderSize,
            @RequestParam(name = "reviewSize", defaultValue = "4") int reviewSize,
            @RequestParam(name = "productSize", defaultValue = "8") int productSize){ 
            
        UserDTO userDTO = userService.getLoggedUserDTO();
        model.addAttribute("user", userDTO);

        Pageable orderPageable = PageRequest.of(orderPageNum, orderSize);
        Pageable reviewPageable = PageRequest.of(reviewPageNum, reviewSize);
        Pageable productPageable = PageRequest.of(productPageNum, productSize);

        Page<OrderDTO> orderPage = orderService.getAllOrdersByUserId(userDTO.id(), orderPageable);
        model.addAttribute("orderPage", orderPage); 
        model.addAttribute("nextPageOrder", orderPage.getNumber() + 1);
        model.addAttribute("previousPageOrder", orderPage.getNumber() - 1);
        model.addAttribute("hasNextOrder", !orderPage.isLast());  
        model.addAttribute("hasPreviousOrder", !orderPage.isFirst());  
        
        Page<ReviewDTO> reviewPage = reviewService.getAllReviewsByUserId(userDTO.id(), reviewPageable);
        model.addAttribute("reviewPage", reviewPage);
        model.addAttribute("nextPageReview", reviewPage.getNumber() + 1);
        model.addAttribute("previousPageReview", reviewPage.getNumber() - 1);
        model.addAttribute("hasNextReview", !reviewPage.isLast());
        model.addAttribute("hasPreviousReview", !reviewPage.isFirst());

        Page<ProductDTO> productPage = productService.getAllProductsByUserId(userDTO.id(), productPageable);
        model.addAttribute("productPage", productPage);
        model.addAttribute("nextPageProduct", productPage.getNumber() + 1);
        model.addAttribute("previousPageProduct", productPage.getNumber() - 1);
        model.addAttribute("hasNextProduct", !productPage.isLast());
        model.addAttribute("hasPreviousProduct", !productPage.isFirst());

        return "profile_template";
    }
     

    @GetMapping("/update")
    public String getEditProfile(Model model) {
        return "profile_update_template";
    }

    @PostMapping("/update")
    public String saveProfileChanges(
            Model model, 
            @Valid @ModelAttribute UserDTO newUserDTO, 
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
            return "profile_update_template";
        }

        UserDTO userDTO = userService.getLoggedUserDTO();

        boolean isUsernameChanged = !userDTO.username().equals(newUserDTO.username());

        userDTO = userService.update(userDTO.id(), newUserDTO);
        
        if (profilePhotoFile != null) {
            try {
                if (!profilePhotoFile.isEmpty()) {
                    ImageDTO oldPhotoDTO = userDTO.profilePhoto();
                    imageService.saveImageInUser(profilePhotoFile);
                    if (oldPhotoDTO != null) imageService.delete(oldPhotoDTO);
                }
            } catch (IOException e) {
                model.addAttribute("imageError", "Error uploading image");
                return "profile_update_template";
            }
        }

        if (isUsernameChanged) {
            // If the username has changed, invalidate the session and clear the security context
            request.getSession().invalidate();
            SecurityContextHolder.clearContext();
            return "redirect:/login";
        }
        return "redirect:/profile";
    }

    @GetMapping("/order/{id}")
    public String getOrderView(Model model, @PathVariable Long id) {
        model.addAttribute("order", orderService.findDTOById(id));
        return "order_template";
    }

    @PostMapping("/order/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return "redirect:/profile";
    }

    @PostMapping("/deleteaccount") 
    public String deleteAccount(Model model, HttpServletRequest request) { 
        UserDTO userDTO = userService.getLoggedUserDTO();
        userService.deleteDTOById(userDTO.id());
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }

}
