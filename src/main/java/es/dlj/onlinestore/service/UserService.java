package es.dlj.onlinestore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.model.UserInfo;
import es.dlj.onlinestore.repository.UserInfoRepository;
import jakarta.annotation.PostConstruct;

@Service
public class UserService {

    @Autowired
    private UserInfoRepository users;
    
    @PostConstruct
    public void init() {
        users.save(new UserInfo("admin", "admin", "adminName", "adminLastName", "admin@gmail.com", UserInfo.Role.ADMIN));
    }

    public UserInfo findByUserNameAndPassword(String userName, String password) {
        return users.findByUserNameAndPassword(userName, password);
    }

    public void save(UserInfo user) {
        users.save(user);
    }

    
}
