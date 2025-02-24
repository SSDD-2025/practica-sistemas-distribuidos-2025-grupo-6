package es.dlj.onlinestore.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;

@Entity
public class Review {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @Size(min = 1, max = 100, message = "Description must be between 1 and 100 characters")	
    private String title;
    
    @Size(min = 3, max = 2000, message = "Description must be between 3 and 2000 characters")	
    private String description;

    private int rating;

    @ManyToOne
    private UserInfo owner;

    @ManyToOne
    private Product product;

    public Review() {

    }

    public Review(String title, String description, int rating, UserInfo owner, Product product) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.owner = owner;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }
    
    public UserInfo getOwner() {
        return owner;
    }

    public Product getProduct() {
        return product;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String tittle) {
        this.title = tittle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setOwner(UserInfo owner) {
        this.owner = owner;
    } 

    public void setProduct(Product product) {
        this.product = product;
    }
    
    @Override
    public String toString() {
        return "UserRating{" + "id=" + id + ", tittle=" + title + ", description=" + description + ", rating=" + rating + ", creationDate=" + creationDate + ", owner=" + owner + '}';
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationDateFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'of' MMMM, yyyy 'at' HH:mm", Locale.ENGLISH);
        return creationDate.format(formatter);
    }
    
}
