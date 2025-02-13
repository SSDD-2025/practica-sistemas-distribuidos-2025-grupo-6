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

    private long userId = 1L;

    public UserInfo getUser() {
        return users.findById(userId).get();
    }

    public void setUser(long userId) {
        this.userId = userId;
    }

}
