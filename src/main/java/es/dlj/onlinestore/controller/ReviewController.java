package es.dlj.onlinestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.Review;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.service.ProductService;
import es.dlj.onlinestore.service.UserComponent;
import es.dlj.onlinestore.service.UserReviewService;


@Controller
@RequestMapping("/review")
class ReviewController {

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private UserReviewService userReviewService;

    @Autowired
    private ProductService productService;

    @PostMapping("/add-review")
    public String submitReview(Model model, @ModelAttribute Review review, @RequestParam Long productId) {
        // Get the user information, the product and save the review in the database
        UserInfo user = userComponent.getUser();
        Product product = productService.getProduct(productId);

        review.setOwner(user);
        review.setProduct(product);
        userReviewService.save(review);
        
        user.addReview(review); // TODO: break encapsulation
        productService.addRatingToProduct(product, review.getRating());
        return "redirect:/userprofile"; 
    }

}
