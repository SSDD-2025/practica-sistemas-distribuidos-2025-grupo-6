package es.dlj.onlinestore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.model.UserInfo;


public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByUserName(String userName);
}
