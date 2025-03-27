package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;
import java.util.List;

import es.dlj.onlinestore.enumeration.ProductType;

public record ProductDTO(
    Long id,
    LocalDateTime creationDate,
    String name,
    String description,
    float price,
    int stock,
    float sale,
    List<ImageDTO> images,
    List<ReviewDTO> reviews,
    UserSimpleDTO seller,
    List<ProductTagSimpleDTO> tags,
    ProductType productType,
    int totalSells,
    UserSimpleDTO owner

) {
    public boolean getInStock(){
        return stock > 0;
    }

    public float getRating() {
        float rating = 0;
        for (ReviewDTO review : reviews) {
            rating += review.rating();
        }
        return ((float) Math.round(rating / ((float) reviews.size()) * 10f)) / 10f;
    }

    public int getNumberRatings() {
        return reviews.size();
    }
    
    public float getProductSale() {
        return ((float) Math.round(price * sale)) / 100f;
    }

    public float getPriceWithSale() {
        return price - getProductSale();
    }

}
