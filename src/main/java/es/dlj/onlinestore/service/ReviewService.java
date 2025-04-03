package es.dlj.onlinestore.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.domain.Review;
import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.dto.ReviewDTO;
import es.dlj.onlinestore.mapper.ReviewMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import es.dlj.onlinestore.repository.ReviewRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page; 


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;


    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;


    @Autowired
    private ReviewMapper reviewMapper;

    @PostConstruct
    @Transactional
    public void init() {
            
        User user = userService.findById(1L);

        List<Product> products = new ArrayList<>(productService.findAll()); 

        List<Review> reviews = new ArrayList<>();
        reviews.add(reviewRepository.save(new Review("Excellent laptop", "The Dell XPS 15 is incredibly fast and efficient. Highly recommended.", 5)));
        reviews.add(reviewRepository.save(new Review("Bad experience", "The iPhone 13 Pro overheats a lot. I don’t recommend it.", 2)));
        reviews.add(reviewRepository.save(new Review("Very good", "The MacBook Air M1 has excellent performance and long battery life.", 5)));
        reviews.add(reviewRepository.save(new Review("Not worth it", "The Samsung Galaxy Watch 4 lacks good compatibility with iOS.", 3)));
        reviews.add(reviewRepository.save(new Review("Amazing sound quality", "The Sony WH-1000XM4 has outstanding noise cancellation.", 5)));
        reviews.add(reviewRepository.save(new Review("Just okay", "The Kindle Paperwhite is good, but the interface is a bit slow.", 3)));
        reviews.add(reviewRepository.save(new Review("Great purchase", "The PlayStation 5 is an incredible console. The graphics are stunning.", 5)));
        reviews.add(reviewRepository.save(new Review("Fragile", "The GoPro Hero 9 broke after a small drop. I wasn’t convinced.", 2)));
        reviews.add(reviewRepository.save(new Review("Good but slow", "The Kindle Paperwhite is perfect for reading, but page transitions are slow.", 3)));
        reviews.add(reviewRepository.save(new Review("Superb sound", "Sony WH-1000XM4 has excellent noise cancelation and sound quality.", 5)));
        reviews.add(reviewRepository.save(new Review("Comfortable and good", "Bose QC35 II is lightweight and has good sound quality.", 4)));
        reviews.add(reviewRepository.save(new Review("Incredible colors", "The LG OLED CX 55 has the best picture quality I’ve ever seen.", 5)));
        reviews.add(reviewRepository.save(new Review("Great but pricey", "Samsung QLED Q80T is fantastic, but a bit overpriced.", 4)));
        reviews.add(reviewRepository.save(new Review("Great for beginners", "The Nikon D3500 takes fantastic pictures with ease.", 5)));
        reviews.add(reviewRepository.save(new Review("Perfect for vlogging", "The Canon EOS M50 is compact and records high-quality videos.", 5)));
        reviews.add(reviewRepository.save(new Review("Durable and reliable", "The GoPro Hero 9 is excellent for action shots.", 5)));
        reviews.add(reviewRepository.save(new Review("Versatile and powerful", "The Surface Pro 7 is perfect for work and creativity.", 5)));
        reviews.add(reviewRepository.save(new Review("Crisp display", "The Dell UltraSharp 27 is amazing for professional work.", 5)));
        reviews.add(reviewRepository.save(new Review("A great smartwatch", "Apple Watch Series 7 is fast and the display is fantastic.", 5)));
        reviews.add(reviewRepository.save(new Review("Good for Android users", "The Samsung Galaxy Watch 4 is great, but battery life is short.", 4)));

        for (int i = 0 ; i < products.size(); i++){
            reviews.get(i).setOwner(user);
            reviews.get(i).setProduct(products.get(i));
            reviewRepository.save(reviews.get(i));
        }
    }

    // public ReviewDTO createReview(ReviewDTO reviewDTO) {
    //     Review review = reviewMapper.toDomain(reviewDTO);
    //     reviewRepository.save(review); 
    //     return reviewMapper.toDTO(review);
    // }

    @Transactional
    public ReviewDTO delete (Long id) {
        Review review = reviewRepository.findById(id).orElseThrow();
        deepDeleteFromOwner(review);
        deepDeleteFromProduct(review);
        reviewRepository.delete(review);
        reviewRepository.flush();
        return reviewMapper.toDTO(review);
    }

    @Transactional
    private void deepDeleteFromProduct(Review review) {
        Product product = review.getProduct();
        if (product != null) {
            product.removeReview(review);
            productService.save(product);
        }
    }

    @Transactional
    private void deepDeleteFromOwner(Review review) {
        User owner = review.getOwner();
        if (owner != null) {
            owner.removeReview(review);
            userService.save(owner);
        }
    }

    @Transactional
    public void save(Long productId, ReviewDTO reviewDTO) {
        User user = userService.getLoggedUser();
        Product product = productService.findById(productId);
        Review review = reviewMapper.toDomain(reviewDTO);
        review.setId(null);
        review.setProduct(product);
        review.setOwner(user);
        reviewRepository.save(review);

        product.addReview(review);
        user.addReview(review);

        productService.save(product);
        userService.save(user);
    }

    public Page<ReviewDTO> findAllByProductIdPag(Long productId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByProductId(productId, pageable);
        return reviews.map(reviewMapper::toDTO);
    }

    public Page<ReviewDTO> findAllReviewsByUserIdPag (Long userId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByOwnerId(userId, pageable);
        return reviews.map(reviewMapper::toDTO);
    }

    public List<ReviewDTO> findAllByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        return reviewMapper.toDTOs(reviews);
    }

    public ReviewDTO findById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        return reviewMapper.toDTO(review);
    }
}
