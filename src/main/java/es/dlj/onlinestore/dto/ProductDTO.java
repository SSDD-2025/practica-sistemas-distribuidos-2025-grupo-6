package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    Integer totalSells

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

    public String getAllTagsInString() {
        return tags.stream()
           .map(ProductTagSimpleDTO::name)
           .collect(Collectors.joining(", "));
    }

    public List<Map<String, Object>> getAllImages() {
        List<Map<String, Object>> imagesMapped = new ArrayList<>();
        for (int i = 0; i < this.images.size(); i++) {
            imagesMapped.add(Map.of("name", this.images.get(i).id(), "selected", (i == 0)));
        }
        return imagesMapped;
    }

    public List<Map<String, Object>> getProductTypesMapped() {
        return ProductType.getMapped(productType);
    }
}
