package es.dlj.onlinestore.model;

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

@Entity
public class UserInfo {

    public void addProductForSale(Product savedProduct) {
        this.productsForSell.add(savedProduct);
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public static enum Role {
        USER, ADMIN, UNREGISTERED
    }

    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDateTime creationDate;

    private String userName;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String address;
    private String city;
    private String postalCode;
    private String phone;
    private String creditCard;
    private String profilePhoto; 

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Product> productsForSell;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderInfo> orders = new ArrayList<>();

    @OneToOne (cascade = CascadeType.ALL)
    private Image profileImage;

    public void addOrder(OrderInfo order){
        orders.add(order);
    }

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Product> cartProducts = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    public UserInfo() {
    }

    public UserInfo(String userName, String password, String name, String surname, String email, Role role, String address, String city, String postalCode, String phone) {
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
        this.productsForSell = new ArrayList<Product>();
    }

    public void updateWith(UserInfo user) {
        this.userName = user.userName != null ? user.userName : this.userName;
        this.password = user.password != null ? user.password : this.password;
        this.name = user.name != null ? user.name : this.name;
        this.surname = user.surname != null ? user.surname : this.surname;
        this.email = user.email != null ? user.email : this.email;
        this.address = user.address != null ? user.address : this.address;
        this.city = user.city != null ? user.city : this.city;
        this.postalCode = user.postalCode != null ? user.postalCode : this.postalCode;
        this.phone = user.phone != null ? user.phone : this.phone;
        this.profilePhoto = user.profilePhoto != null ? user.profilePhoto : this.profilePhoto;
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

    public String getPhone() {
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
    
    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review){
        this.reviews.add(review);
        review.setOwner(this);
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

    public List<OrderInfo> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderInfo> orders) {
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

    // public void setPaymentMethod(PaymentMethod paymentMethod) {
    //     this.paymentMethod = paymentMethod;
    // }

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
            paymentMethods.add(Map.of("name", pMethod.getName(), "selected", (paymentMethod != null && paymentMethod.equals(pMethod))));
        }
        return paymentMethods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo otherUser = (UserInfo) o;
        return Objects.equals(id, otherUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addProduct(Product product){
        this.productsForSell.add(product);
    }

    public List<Product> getProductsForSell(){
        return this.productsForSell;
    }

    public void removeProduct(Product removingProduct){
        int index = 0;
        for (int i=0; i<this.productsForSell.size(); i++){
            Product product = this.productsForSell.get(i);
            if (removingProduct.getId() == product.getId())
            {
                index = i;
                break;
            }
        }
        this.productsForSell.remove(index);
    }
    
    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", phone='" + phone + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", reviews=" + reviews +
                ", orders=" + orders +
                ", cartProducts=" + cartProducts +
                ", role=" + role +
                '}';	
    }

    public String getCreationDateFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'of' MMMM, yyyy 'at' HH:mm", Locale.ENGLISH);
        return creationDate.format(formatter);
    }
}