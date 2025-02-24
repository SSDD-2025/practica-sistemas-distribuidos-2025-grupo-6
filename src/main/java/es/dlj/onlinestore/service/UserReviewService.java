package es.dlj.onlinestore.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.Review;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.UserReviewRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class UserReviewService {

    @Autowired
    private UserReviewRepository userReviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @PostConstruct
    @Transactional
    public void init() {
            
        UserInfo user1 = userService.findById(2L); 
        UserInfo user2 = userService.findById(3L); 
        UserInfo user3 = userService.findById(4L); 

        Product product1 = productService.getProduct(1L); 
        Product product2 = productService.getProduct(2L); 
        Product product3 = productService.getProduct(3L); 
        Product product4 = productService.getProduct(4L); 
        Product product5 = productService.getProduct(5L); 
        Product product6 = productService.getProduct(6L); 
        Product product7 = productService.getProduct(7L); 
        Product product8 = productService.getProduct(8L); 
        Product product9 = productService.getProduct(9L); 
        Product product10 = productService.getProduct(10L); 
        Product product11 = productService.getProduct(11L); 
        Product product12 = productService.getProduct(12L); 
        Product product13 = productService.getProduct(13L); 
        Product product14 = productService.getProduct(14L); 
        Product product15 = productService.getProduct(15L); 
        Product product16 = productService.getProduct(16L); 
        Product product17 = productService.getProduct(17L); 
        Product product18 = productService.getProduct(18L); 
        Product product19 = productService.getProduct(19L); 
        Product product20 = productService.getProduct(20L); 

        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review("Excellent laptop", "The Dell XPS 15 is incredibly fast and efficient. Highly recommended.", 5, user2, product1));
        reviews.add(new Review("I don't like it", "I don't recommend this laptop because it's very slow.", 2, user1, product1));
        reviews.add(new Review("Bad experience", "The iPhone 13 Pro overheats a lot. I don’t recommend it.", 2, user3, product2));
        reviews.add(new Review("I love this phone", "The iPhone 13 Pro takes very good photos and is incredibly fast.", 5, user2, product2));
        reviews.add(new Review("Very good", "The MacBook Air M1 has excellent performance and long battery life.", 5, user1, product3));
        reviews.add(new Review("Not worth it", "The Samsung Galaxy Watch 4 lacks good compatibility with iOS.", 3, user1, product4));
        reviews.add(new Review("Good", "I recommend it.", 4, user3, product4));
        reviews.add(new Review("Amazing sound quality", "The Sony WH-1000XM4 has outstanding noise cancellation.", 5, user2, product5));
        reviews.add(new Review("Just okay", "The Kindle Paperwhite is good, but the interface is a bit slow.", 3, user3, product6));
        reviews.add(new Review("Great purchase", "The PlayStation 5 is an incredible console. The graphics are stunning.", 5, user2, product7));
        reviews.add(new Review("Fragile", "The GoPro Hero 9 broke after a small drop. I wasn’t convinced.", 2, user1, product8));
        reviews.add(new Review("I love it", "It's an excellent product", 5, user3, product8));
        reviews.add(new Review("Powerful and sleek", "The Dell XPS 15 is an amazing laptop with top-tier performance.", 5, user2, product1));
        reviews.add(new Review("Too expensive", "The iPhone 13 Pro is good, but I don’t think it justifies the price.", 3, user3, product2));
        reviews.add(new Review("Excellent smartphone", "The Samsung Galaxy S21 is fast, has a great camera, and is well-priced.", 5, user1, product3));
        reviews.add(new Review("Decent budget laptop", "The HP Pavilion 14 is good for basic use, but don’t expect too much.", 4, user1, product4));
        reviews.add(new Review("Best laptop I’ve owned", "MacBook Air M1 is insanely fast and battery lasts forever!", 5, user2, product5));
        reviews.add(new Review("Worth every penny", "The PlayStation 5 is the best console of this generation!", 5, user3, product6));
        reviews.add(new Review("Impressed", "The Xbox Series X runs smoothly, and Game Pass is a great deal.", 4, user2, product7));
        reviews.add(new Review("Perfect for work", "The iPad Air 4 is lightweight, fast, and works great for multitasking.", 5, user1, product8));
        reviews.add(new Review("Good but slow", "The Kindle Paperwhite is perfect for reading, but page transitions are slow.", 3, user2, product9));
        reviews.add(new Review("Superb sound", "Sony WH-1000XM4 has excellent noise cancelation and sound quality.", 5, user3, product10));
        reviews.add(new Review("Comfortable and good", "Bose QC35 II is lightweight and has good sound quality.", 4, user3, product11));
        reviews.add(new Review("Incredible colors", "The LG OLED CX 55 has the best picture quality I’ve ever seen.", 5, user1, product12));
        reviews.add(new Review("Great but pricey", "Samsung QLED Q80T is fantastic, but a bit overpriced.", 4, user2, product13));
        reviews.add(new Review("Great for beginners", "The Nikon D3500 takes fantastic pictures with ease.", 5, user3, product14));
        reviews.add(new Review("Perfect for vlogging", "The Canon EOS M50 is compact and records high-quality videos.", 5, user1, product15));
        reviews.add(new Review("Durable and reliable", "The GoPro Hero 9 is excellent for action shots.", 5, user1, product16));
        reviews.add(new Review("Versatile and powerful", "The Surface Pro 7 is perfect for work and creativity.", 5, user2, product17));
        reviews.add(new Review("Crisp display", "The Dell UltraSharp 27 is amazing for professional work.", 5, user3, product18));
        reviews.add(new Review("A great smartwatch", "Apple Watch Series 7 is fast and the display is fantastic.", 5, user2, product19));
        reviews.add(new Review("Good for Android users", "The Samsung Galaxy Watch 4 is great, but battery life is short.", 4, user1, product20));

        for (Review review : reviews) {
            userReviewRepository.save(review);
            review.getOwner().addReview(review);
        }
    }

    public List<Review> getUserRatings(UserInfo owner) {
        return userReviewRepository.findByOwner(owner);
    }

    public List<Review> getReviewsByProduct(Product product) {
        return userReviewRepository.findByProduct(product);
    }

    public void save(Review review) {
        userReviewRepository.save(review);
    }

    // public float getAverageRatingForProduct(Product product) {
    //     Float average = userReviewRepository.getAverageRating(product);
    //     return (float) ((average != null) ? average : 0.0); 
    // }

    // public float findByProductAndGetAvgRating(Product product) {
    //     return userReviewRepository.findByProductAndGetAvgRating(product);
    // }
   


}
