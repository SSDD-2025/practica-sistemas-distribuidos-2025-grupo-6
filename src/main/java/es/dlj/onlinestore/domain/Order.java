package es.dlj.onlinestore.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "OrderInfo")
public class Order {
    
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private LocalDateTime creationDate;
    
    @ManyToOne
    private User user;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Product> products = new HashSet<>();

    private List<String> nonContinuedProducts = new ArrayList<>();
    
    private Float totalPrice;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String address;

    private String phoneNumber;
    
    public Order() {}

    public Order(Float totalPrice, PaymentMethod paymentMethod, String address, String phoneNumber) {
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Set<Product> getProducts() { return products; }
    public void setProducts(Set<Product> products) { this.products = products; }

    public Float getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Float totalPrice) { this.totalPrice = totalPrice; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public String getCreationDateFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'of' MMMM, yyyy 'at' HH:mm", Locale.ENGLISH);
        return creationDate.format(formatter);
    }

    public String getCreationDateFormattedSimpler() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
        return creationDate.format(formatter);
    }

    public void removeProduct(Product product) {
        if (products.contains(product)) {
            products.remove(product);
        }
        nonContinuedProducts.add(product.getName());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", user=" + user +
                ", products=" + products +
                ", totalPrice=" + totalPrice +
                ", paymentMethod=" + paymentMethod +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Order)) return false;
        Order other = (Order) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public List<String> getNonContinuedProducts() {
        return nonContinuedProducts;
    }

    public void setNonContinuedProducts(List<String> nonContinuedProducts) {
        this.nonContinuedProducts = nonContinuedProducts;
    }
}
