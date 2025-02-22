/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package es.dlj.onlinestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.dlj.onlinestore.model.Review;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.UserRatingRepository;
import es.dlj.onlinestore.service.UserComponent;


@Controller
@RequestMapping("/review")
class ReviewController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private UserRatingRepository userRatingRepository;

    @PostMapping("/add-review")
    public String submitReview(Model model, @ModelAttribute Review review) {
        UserInfo user = userComponent.getUser();
        review.setOwner(user);
        userRatingRepository.save(review);
        user.addReview(review);

        return "redirect:/userprofile"; 
    }

}
