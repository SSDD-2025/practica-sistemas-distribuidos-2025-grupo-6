package es.dlj.onlinestore.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import es.dlj.onlinestore.enumeration.ProductType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
public class Product {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    private String name;

    @Size(min = 3, max = 2000, message = "Description must be between 3 and 2000 characters")	
    private String description;

    @Positive(message = "Price must be positive")
    private Float price;

    @PositiveOrZero(message = "Stock must be positive or zero")
    private int stock;

    @Range(min = 0, max = 100, message = "Sale must be between 0 and 100")
    private float sale;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy="product", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne
    private User seller;
    
    @ManyToMany
    private List<ProductTag> tags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    private int totalSells = 0;

    public Product() {}

    public Product(String name, float price, String description, ProductType productType, int stock, float sale){
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.productType = productType;
        this.sale = sale;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getStock() {
        return stock;
    }

    public ProductType getProductType() {
        return productType;
    }

    public int getTotalSells() {
        return totalSells;
    }

    public float getSale() {
        return sale;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public void setTotalSells(int totalSells) {
        this.totalSells = totalSells;
    }

    public void setSale(float sale) {
        this.sale = sale;
    }

    public List<ProductTag> getTags() {
        return tags;
    }

    public void setTags(List<ProductTag> tags) {
        this.tags = tags;
    }

    public void addImage(Image image){
        this.images.add(image);
    }

    public List<Image> getImages(){
        return this.images;
    }

    public void addTag(ProductTag tag){
        this.tags.add(tag);
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void clearImages() {
        this.images.clear();
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public boolean sellOneUnit() {
        if (stock > 0) {
            stock--;
            totalSells++;
            return true;
        }
        return false;
    }

    public boolean isInStock() {
        return stock > 0;
    }

    public float getRating() {
        float rating = 0;
        for (Review review : reviews) {
            rating += review.getRating();
        }
        return ((float) Math.round(rating / ((float) reviews.size()) * 10f)) / 10f;
    }

    public int getNumberRatings() {
        return reviews.size();
    }

    public List<Map<String, Object>> getProductTypesMapped() {
        return ProductType.getMapped(productType);
    }

    public boolean isOnSale() {
        return sale > 0;
    }

    public int getTagsCount() {
        return tags.size();
    }

    public float getPriceWithSale() {
        return price - getProductSale();
    }

    public float getProductSale() {
        return ((float) Math.round(price * sale)) / 100f;
    }

    public String getAllTagsInString() {
        return tags.stream()
           .map(ProductTag::getName)
           .collect(Collectors.joining(", "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", sale=" + sale +
                ", images=" + images +
                ", reviews=" + reviews +
                ", seller=" + seller +
                ", tags=" + tags +
                ", productType=" + productType +
                '}';
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    public void removeImage(Image image) {
        this.images.remove(image);
    }
}