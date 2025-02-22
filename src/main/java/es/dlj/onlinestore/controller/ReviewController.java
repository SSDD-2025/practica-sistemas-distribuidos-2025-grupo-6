package es.dlj.onlinestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.model.Review;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.UserRatingRepository;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserComponent;


@Controller
@RequestMapping("/review")
class ReviewController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private UserRatingRepository userRatingRepository;

    @Autowired
    private ProductService productService;

    @PostMapping("/add-review")
    public String submitReview(Model model, @ModelAttribute Review review, @RequestParam Long productId) {
        UserInfo user = userComponent.getUser();
        review.setOwner(user);
        review.setProduct(productService.getProduct(productId));
        userRatingRepository.save(review);
        user.addReview(review);

        return "redirect:/userprofile"; 
    }

}
