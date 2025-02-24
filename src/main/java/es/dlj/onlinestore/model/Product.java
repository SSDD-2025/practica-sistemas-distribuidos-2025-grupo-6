package es.dlj.onlinestore.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;


import es.dlj.onlinestore.enumeration.ProductType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;

@Entity
public class Product {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    private String name;
    @Positive(message = "Price must be positive")
    private Float price;
    @PositiveOrZero(message = "Stock must be positive or zero")
    private int stock;
    

    @Size(min = 3, max = 2000, message = "Description must be between 3 and 2000 characters")	
    private String description;

    @OneToMany (cascade = CascadeType.ALL)
    private List<Image> images = new LinkedList<>();

    @ManyToOne
    private UserInfo seller;
    
    @ManyToMany
    private List<ProductTag> tags;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Min(value = 0, message = "Rating must be positive")
    @Max(value = 5, message = "Rating must be less than 5")
    private float rating = 0;

    private int numberReviews = 0;
    private int totalSells = 0;
    private int lastWeekSells = 0;

    @Min(value = 0, message = "Sale must be positive")
    @Max(value = 100, message = "Sale must be less than 100%")
    private float sale;

    public Product(){

    }

    public Product(String name, float price, String description, ProductType productType, int stock, List<ProductTag> tags, float sale){
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.productType = productType;
        this.sale = sale;
        this.tags = tags;
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

    public float getRating() {
        return rating;
    }

    public int getNumberRatings() {
        return numberReviews;
    }

    public int getTotalSells() {
        return totalSells;
    }

    public int getLastWeekSells() {
        return lastWeekSells;
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

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setNumberRatings(int numberRatings) {
        this.numberReviews = numberRatings;
    }

    public void setTotalSells(int totalSells) {
        this.totalSells = totalSells;
    }

    public void setLastWeekSells(int lastWeekSells) {
        this.lastWeekSells = lastWeekSells;
    }

    public void setSale(float sale) {
        this.sale = sale;
    }

    public void addRating(float rating){
        this.rating = (this.rating * this.numberReviews + rating) / (this.numberReviews + 1);
        this.numberReviews += 1;
    }

    public void addSells(int sells){
        this.totalSells += sells;
        this.lastWeekSells += sells;
    }

    public void addSale(int sale){
        this.sale += sale;
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


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                ", productType=" + productType +
                ", rating=" + rating +
                ", numberRatings=" + numberReviews +
                ", totalSells=" + totalSells +
                ", lastWeekSells=" + lastWeekSells +
                ", sale=" + sale +
                '}';
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    public List<Map<String, Object>> getProductTypesMapped() {
        return ProductType.getMapped(productType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isOnSale() {
        return sale > 0;
    }

    public int getTagsCount() {
        return tags.size();
    }

    public float getPriceWithSale() {
        return price * (1 - sale / 100);
    }

    public UserInfo getSeller() {
        return seller;
    }

    public void setSeller(UserInfo seller) {
        this.seller = seller;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getAllTagsInString() {
        return tags.stream()
           .map(ProductTag::getName)
           .collect(Collectors.joining(", "));
    }

    public void clearImages() {
        this.images.clear();
    }
}