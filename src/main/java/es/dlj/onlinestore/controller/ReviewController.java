/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package es.dlj.onlinestore.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import es.dlj.onlinestore.model.Review;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.UserRatingRepository;
import es.dlj.onlinestore.service.UserComponent;
import es.dlj.onlinestore.service.UserRatingService;


@Controller
@RequestMapping("/review")
class ReviewController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private UserRatingService userRatingService;

    @Autowired
    private UserRatingRepository userRatingRepository;

    @PostMapping("/makereview")
    public String submitReview(Model model, 
                               @RequestParam String title,
                               @RequestParam String description,
                               @RequestParam String rating ) {
    
        UserInfo user = userComponent.getUser();
        model.addAttribute("user", user); 
                               
        Review review = new Review(title, description, Integer.parseInt(rating), userComponent.getUser());
        userRatingRepository.save(review);
        model.addAttribute("reviews", review);
        
        user.addReview(review);

        return "redirect:/userprofile"; 
    }

}
