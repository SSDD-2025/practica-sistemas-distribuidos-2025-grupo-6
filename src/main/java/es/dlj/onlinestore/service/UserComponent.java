package es.dlj.onlinestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.UserInfoRepository;

@Component
@SessionScope
public class UserComponent {

    @Autowired
    private UserInfoRepository users;

    private Long userId = 1L;
    private UserInfo user;

    public UserInfo getUser() {
        if (this.user == null || !this.user.getId().equals(userId)) {
            this.user = users.findById(userId).get();
        }
        return this.user;
    }

    public boolean isLoggedUser() {
        return userId != null;
    }

    public void setUser(Long userId) {
        this.userId = userId;
        this.user = users.findById(userId).get();
    }

    public void addProductToCart(Product product) {
        getUser();
        this.user.addProductToCart(product);
    }

    public void removeProductFromCart(Product product) {
        getUser();
        this.user.removeProductFromCart(product);
    }

    public void clearCart() {
        getUser();
        this.user.clearCart();
    }

    public void addProductForSale(Product savedProduct) {
        getUser();
        this.user.addProductForSale(savedProduct);
    }
}
