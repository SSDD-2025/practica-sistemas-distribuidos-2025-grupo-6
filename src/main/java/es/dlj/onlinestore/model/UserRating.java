package es.dlj.onlinestore.model;
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

    private String tittle;
    private String description;
    private int rating;
    private String date;

    @ManyToOne
    private UserInfo owner;

    public UserRating() {
    }

    public UserRating(String tittle, String description, int rating, String date, UserInfo owner) {
        this.tittle = tittle;
        this.description = description;
        this.rating = rating;
        this.date = date;
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

    public String getDate() {
        return date;
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

    public void setDate(String date) {
        this.date = date;
    }

    public void setOwner(UserInfo owner) {
        this.owner = owner;
    } 
    
    @Override
    public String toString() {
        return "UserRating{" + "id=" + id + ", tittle=" + tittle + ", description=" + description + ", rating=" + rating + ", date=" + date + ", owner=" + owner + '}';
    }
    
}
