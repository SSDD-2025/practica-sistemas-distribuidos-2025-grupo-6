package es.dlj.onlinestore.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserRating {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String tittle;
    private String description;
    private int rating;
    private String date;
    private String userName;

    public UserRating() {
    }

    public UserRating(String tittle, String description, int rating, String date, String userName) {
        this.tittle = tittle;
        this.description = description;
        this.rating = rating;
        this.date = date;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }    
    
    @Override
    public String toString() {
        return "UserRating{" + "id=" + id + ", tittle=" + tittle + ", description=" + description + ", rating=" + rating + ", date=" + date + ", userName=" + userName + '}';
    }
    
}
