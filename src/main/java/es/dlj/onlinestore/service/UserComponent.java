package es.dlj.onlinestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.UserInfoRepository;
import jakarta.annotation.PostConstruct;

@Component
public class UserComponent {

    @PostConstruct
    public void init() {
        users.save(new UserInfo("admin", "admin", "adminName", "adminLastName", "admin@gmail.com", UserInfo.Role.ADMIN));
    }

    @Autowired
    private UserInfoRepository users;

    public UserInfo getUser() {
        return users.findById(1L).get();
    }



}
