package es.dlj.onlinestore.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import es.dlj.onlinestore.enumeration.PaymentMethod;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "UserInfo")
public class User {

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDateTime creationDate;

    private String username;
    private String encodedPassword;
    private String name;
    private String surname;
    private String email;
    private String address;
    private String city;
    private String postalCode;
    private String phone;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Product> productsForSell = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Order> orders = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Image profilePhoto;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Product> cartProducts = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<String>();

    public User() {}

    public User(String username, String password, String name, String surname, String email, List<String> role, String address, String city, String postalCode, String phone) {
        this.username = username;
        this.encodedPassword = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.roles = role;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEncodedPassword(String password) {
        this.encodedPassword = password;
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

    public List<String> getRoles() {
        return roles;
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

    public String getPhone() {
        return phone;
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

    public void setRoles(List<String> role) {
        this.roles = role;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review){
        this.reviews.add(review);
        
    }

    public void removeReview(Review review){
        reviews.remove(review);
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Set<Product> getCartProducts() {
        return cartProducts;
    }

    public void addProductToCart(Product product){
        cartProducts.add(product);
    }

    public void removeProductFromCartById(long productId){
        Product product = cartProducts.stream().filter(p -> productId == p.getId()).findFirst().orElse(null);
        if (product == null) return;
        cartProducts.remove(product);
    }

    public void clearCart() {
        cartProducts.clear();
    }

    public void setCartProducts(Set<Product> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = PaymentMethod.fromString(paymentMethod);
    }

    public boolean isCartUsed() {
        return !cartProducts.isEmpty();
    }

    public int getCartSize() {
        return cartProducts.size();
    }

    public void clearCartProducts() {
        cartProducts.clear();
    }

    public float getCartTotalPrice() {
        return (float) cartProducts.stream().mapToDouble(Product::getPriceWithSale).sum();
    }

    public List<Product> getProductsForSell(){
        return this.productsForSell;
    }

    public void addOrder(Order order){
        orders.add(order);
    }

    public void addProductForSale(Product savedProduct) {
        this.productsForSell.add(savedProduct);
    }

    public void removeOrder(Order order) {
        this.orders.remove(order);
    }

    public String getCreationDateFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'of' MMMM, yyyy 'at' HH:mm", Locale.ENGLISH);
        return creationDate.format(formatter);
    }

    public void setProductsForSell(List<Product> productsForSell) {
        this.productsForSell = productsForSell;
    }

    public Image getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Image profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void removeProductFromSales(Product product) {
        this.productsForSell.remove(product);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", username='" + username + '\'' +
                ", password='" + encodedPassword + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", phone='" + phone + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", role=" + roles +
                '}';    
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User otherUser = (User) o;
        return Objects.equals(id, otherUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
