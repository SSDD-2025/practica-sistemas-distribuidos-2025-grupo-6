package es.dlj.onlinestore.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class UserInfo {

    public static enum Role {
        USER, ADMIN, UNREGISTERED
    }

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userName;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String address;
    private String city;
    private String postalCode;
    private int phone;
    private String creditCard;
    private String profilePhoto; 

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<UserRating> reviews = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    public UserInfo() {
    }

    public UserInfo(String userName, String password, String name, String surname, String email, Role role, String address, String city, String postalCode, int phone) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.role = role;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public int getPhone() {
        return phone;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
    
    public List<UserRating> getReviews() {
        return reviews;
    }

    public void addReview(UserRating review){
        reviews.add(review);
    }

    public void removeReview(UserRating review){
        reviews.remove(review);
    }
   
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    
}