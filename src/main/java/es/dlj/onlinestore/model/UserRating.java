package es.dlj.onlinestore.model;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class UserRating {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDateTime creationDate;

    private String tittle;
    private String description;
    private int rating;

    @ManyToOne
    private UserInfo owner;

    public UserRating() {
    }

    public UserRating(String tittle, String description, int rating, UserInfo owner) {
        this.tittle = tittle;
        this.description = description;
        this.rating = rating;
        this.owner = owner;
    }
    
    public Long getId() {
        return id;
    }

    public String getTittle() {
        return tittle;
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

    public void setTittle(String tittle) {
        this.tittle = tittle;
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
        return "UserRating{" + "id=" + id + ", tittle=" + tittle + ", description=" + description + ", rating=" + rating + ", creationDate=" + creationDate + ", owner=" + owner + '}';
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    
}
