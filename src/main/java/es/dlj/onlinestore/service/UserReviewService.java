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
            
        UserInfo user = userService.findById(1L); 

        ArrayList<Product> products = (ArrayList<Product>) productService.getAllProducts(); 

        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review("Excellent laptop", "The Dell XPS 15 is incredibly fast and efficient. Highly recommended.", 5, user, products.get(0)));
        reviews.add(new Review("Bad experience", "The iPhone 13 Pro overheats a lot. I don’t recommend it.", 2, user, products.get(1)));
        reviews.add(new Review("Very good", "The MacBook Air M1 has excellent performance and long battery life.", 5, user, products.get(2)));
        reviews.add(new Review("Not worth it", "The Samsung Galaxy Watch 4 lacks good compatibility with iOS.", 3, user, products.get(3)));
        reviews.add(new Review("Amazing sound quality", "The Sony WH-1000XM4 has outstanding noise cancellation.", 5, user, products.get(4)));
        reviews.add(new Review("Just okay", "The Kindle Paperwhite is good, but the interface is a bit slow.", 3, user, products.get(5)));
        reviews.add(new Review("Great purchase", "The PlayStation 5 is an incredible console. The graphics are stunning.", 5, user, products.get(6)));
        reviews.add(new Review("Fragile", "The GoPro Hero 9 broke after a small drop. I wasn’t convinced.", 2, user, products.get(7)));
        reviews.add(new Review("Good but slow", "The Kindle Paperwhite is perfect for reading, but page transitions are slow.", 3, user, products.get(8)));
        reviews.add(new Review("Superb sound", "Sony WH-1000XM4 has excellent noise cancelation and sound quality.", 5, user, products.get(9)));
        reviews.add(new Review("Comfortable and good", "Bose QC35 II is lightweight and has good sound quality.", 4, user, products.get(10)));
        reviews.add(new Review("Incredible colors", "The LG OLED CX 55 has the best picture quality I’ve ever seen.", 5, user, products.get(11)));
        reviews.add(new Review("Great but pricey", "Samsung QLED Q80T is fantastic, but a bit overpriced.", 4, user, products.get(12)));
        reviews.add(new Review("Great for beginners", "The Nikon D3500 takes fantastic pictures with ease.", 5, user, products.get(13)));
        reviews.add(new Review("Perfect for vlogging", "The Canon EOS M50 is compact and records high-quality videos.", 5, user, products.get(14)));
        reviews.add(new Review("Durable and reliable", "The GoPro Hero 9 is excellent for action shots.", 5, user, products.get(15)));
        reviews.add(new Review("Versatile and powerful", "The Surface Pro 7 is perfect for work and creativity.", 5, user, products.get(16)));
        reviews.add(new Review("Crisp display", "The Dell UltraSharp 27 is amazing for professional work.", 5, user, products.get(17)));
        reviews.add(new Review("A great smartwatch", "Apple Watch Series 7 is fast and the display is fantastic.", 5, user, products.get(18)));
        reviews.add(new Review("Good for Android users", "The Samsung Galaxy Watch 4 is great, but battery life is short.", 4, user, products.get(19)));

        for (int i = 0 ; i < reviews.size(); i++){
            userReviewRepository.save(reviews.get(i));
            reviews.get(i).getOwner().addReview(reviews.get(i));
            productService.addRatingToProduct(products.get(i), reviews.get(i).getRating());
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

    /*
    public float getAverageRatingForProduct(Product product) {
        Float average = userReviewRepository.getAverageRating(product);
        return (float) ((average != null) ? average : 0.0); 
    }*/

    /*
    public float findByProductAndGetAvgRating(Product product) {
         return userReviewRepository.findByProductAndGetAvgRating(product);
    }*/
   


}
