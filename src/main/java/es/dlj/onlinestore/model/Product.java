package es.dlj.onlinestore.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

    public static enum productType{
        NEW,
        RECONDITIONED,
        SECONDHAND
    }

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private Float price;
    private String description;
    private int stock;
    private List<String> tags;
    @Enumerated (EnumType.STRING)
    private productType productType;
    private float rating;
    private int numberRatings;
    private int totalSells;
    private int lastWeekSells;
    private int sale;

    public Product(){
    }

    public Product(String name, float price, String description, productType productType, int stock, List<String> tags){
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.productType = productType;
        this.rating = 0;
        this.numberRatings = 0;
        this.totalSells = 0;
        this.lastWeekSells = 0;
        this.sale = 0;
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

    public productType getProductType() {
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

    public int getSale() {
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

    public void setProductType(productType productType) {
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

    public void setSale(int sale) {
        this.sale = sale;
    }

    public void addRating(float rating){
        this.rating = (this.rating * this.numberRatings + rating)/(this.numberRatings + 1);
        this.numberRatings += 1;
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
}
