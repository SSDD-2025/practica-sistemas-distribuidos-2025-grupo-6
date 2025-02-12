package es.dlj.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.model.UserInfo;


public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    boolean existsByUserNameAndPassword(String userName, String password);

}
