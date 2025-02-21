package es.dlj.onlinestore.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;

import es.dlj.onlinestore.enumeration.ProductType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Product {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;

    @CreationTimestamp
    private LocalDateTime creationDate;

    private String name;
    private Float price;
    private int stock;

    @Column(length = 2000)
    private String description;

    @OneToMany
    private List<Image>images;
    
    @ManyToMany
    private List<ProductTag> tags;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    private float rating;
    private int numberRatings;
    private int totalSells;
    private int lastWeekSells;
    private float sale;

    public Product(){

    }

    public Product(String name, float price, String description, ProductType productType, int stock, List<ProductTag> tags){
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.productType = productType;
        this.rating = 0;
        this.numberRatings = 0;
        this.totalSells = 0;
        this.lastWeekSells = 0;
        this.sale = 0f;
        this.tags = tags;
        this.images = new LinkedList<Image>();
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
        return numberRatings;
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
        this.numberRatings = numberRatings;
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
        this.rating = (this.rating * this.numberRatings + rating)/(this.numberRatings + 1);
        this.numberRatings += 1;
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
                ", numberRatings=" + numberRatings +
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
}
