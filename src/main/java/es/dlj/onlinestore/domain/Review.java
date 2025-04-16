package es.dlj.onlinestore.domain;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Review {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDateTime creationDate;

    private String title;
    private String description;
    private int rating;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Product product;

    public Review() {
        this.creationDate = LocalDateTime.now();
    }

    public Review(String title, String description, int rating) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.creationDate = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String tittle) { this.title = tittle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; } 

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    
    public LocalDateTime getCreationDate() { return creationDate; }

    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public String getCreationDateFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'of' MMMM, yyyy 'at' HH:mm", Locale.ENGLISH);
        return creationDate.format(formatter);
    }

    @Override
    public String toString() {
        return "UserRating{" + 
            "id=" + id + 
            ", tittle=" + title + 
            ", description=" + description + 
            ", rating=" + rating + 
            ", creationDate=" + creationDate + 
            ", owner=" + owner + 
            '}';
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
