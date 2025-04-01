package es.dlj.onlinestore.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.CreationTimestamp;
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

@Entity
public class Product {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;

    @CreationTimestamp
    private LocalDateTime creationDate;

    private String name;
    private String description;
    private float price;
    private int stock;
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

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public float getSale() { return sale; }
    public void setSale(float sale) { this.sale = sale; }

    public List<Image> getImages() { return this.images; }
    public void setImages(List<Image> images) { this.images = images; }
    public void addImage(Image image) { this.images.add(image); }
    public void clearImages() { 
        if (this.images == null) {
            this.images = new ArrayList<>();
        } else {
            this.images.clear(); 
        }
    }
    
    public ProductType getProductType() { return productType; }
    public void setProductType(ProductType productType) { this.productType = productType; }

    public int getTotalSells() { return totalSells; }
    public void setTotalSells(int totalSells) { this.totalSells = totalSells; }

    public List<ProductTag> getTags() { return tags; }
    public void setTags(List<ProductTag> tags) { this.tags = tags; }
    public void addTag(ProductTag tag) { this.tags.add(tag); }

    public User getSeller() { return seller; }
    public void setSeller(User seller) { this.seller = seller; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }
    public void addReview(Review review) { this.reviews.add(review); }
    public void removeReview(Review review) { this.reviews.remove(review); }

    public void sellOneUnit() {
        stock--;
        totalSells++;
    }

    public float getPriceWithSale() {
        return price - ((float) Math.round(price * sale)) / 100f;
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
}