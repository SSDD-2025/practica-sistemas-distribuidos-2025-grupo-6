package es.dlj.onlinestore.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

    public User() {
    }

    public User(String userName, String password, String name, String surname, String email, List<String> role, String address, String city, String postalCode, String phone) {
        this.username = userName;
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

    public void updateWith(User user) {
        this.username = user.username != null ? user.username : this.username;
        this.encodedPassword = user.encodedPassword != null ? user.encodedPassword : this.encodedPassword;
        this.name = user.name != null ? user.name : this.name;
        this.surname = user.surname != null ? user.surname : this.surname;
        this.email = user.email != null ? user.email : this.email;
        this.address = user.address != null ? user.address : this.address;
        this.city = user.city != null ? user.city : this.city;
        this.postalCode = user.postalCode != null ? user.postalCode : this.postalCode;
        this.phone = user.phone != null ? user.phone : this.phone;
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

    public void setUsername(String userName) {
        this.username = userName;
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

    public void removeProductFromCart(Product product){
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

    public List<Map<String, Object>> getPaymentMethodsMapped() {
        List<Map<String, Object>> paymentMethods = new ArrayList<>();
        for (PaymentMethod pMethod : PaymentMethod.values()) {
            paymentMethods.add(Map.of("name", pMethod.toString(), "selected", (paymentMethod != null && paymentMethod.equals(pMethod))));
        }
        return paymentMethods;
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
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", userName='" + username + '\'' +
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

    public void removeProductFromSale(Product product) {
        this.productsForSell.remove(product);
    }
}
