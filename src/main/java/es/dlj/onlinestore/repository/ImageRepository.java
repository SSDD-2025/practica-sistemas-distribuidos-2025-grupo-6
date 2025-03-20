package es.dlj.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{

}
