package es.dlj.onlinestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.UserInfoRepository;

@Component
@SessionScope
public class UserComponent {

    @Autowired
    private UserInfoRepository users;

    private Long userId = 1L;
    //private UserInfo user;

    public UserInfo getUser() {
        return users.findById(userId).get();
    }

    public boolean isLoggedUser() {
        return userId != null;
    }

    public void setUser(Long userId) {
        this.userId = userId;
        /*
        this.user = new UserInfo(); 
        this.user.setId(userId);
        */
    }

}
