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
        users.save(new UserInfo("admin", "admin", "adminName", "adminLastName", "admin@gmail.com", UserInfo.Role.ADMIN, null, null, null, null));
        users.save(new UserInfo("lidiabudios", "1234", "Lidia", "Budios", "l.budios@gmail.com", UserInfo.Role.USER, null, null, null, null));
        users.save(new UserInfo("jaimeochoa", "9876", "Jaime", "Ochoa", "j.ochoa@gmail.com", UserInfo.Role.USER, null, null, null, null));
        users.save(new UserInfo("davidpimentel", "1a1a", "David", "Pimentel", "d.pimentel@gmail.com", UserInfo.Role.USER, null, null, null, null));


    }

    public UserInfo findByUserNameAndPassword(String userName, String password) {
        return users.findByUserNameAndPassword(userName, password);
    }

    public void save(UserInfo user) {
        users.save(user);
    }

    public UserInfo findById(Long id) {
        return users.findById(id).orElse(null);
    }
}