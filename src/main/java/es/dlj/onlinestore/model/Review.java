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
    private UserInfo owner;

    public Review() {

    }

    public Review(String title, String description, int rating, UserInfo owner) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.owner = owner;
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
