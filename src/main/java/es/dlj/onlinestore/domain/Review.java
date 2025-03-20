package es.dlj.onlinestore.domain;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

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

    @Size(min = 3, max = 100, message = "Description must be between 3 and 100 characters")	
    private String title;
    
    @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters")	
    private String description;

    @Range(min = 0, max = 5, message = "Rating must be between 0 and 5")
    private int rating;

    @ManyToOne
    private UserInfo owner;

    @ManyToOne
    private Product product;

    public Review() {}

    public Review(String title, String description, int rating) {
        this.title = title;
        this.description = description;
        this.rating = rating;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review otherReview = (Review) o;
        return id.equals(otherReview.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
