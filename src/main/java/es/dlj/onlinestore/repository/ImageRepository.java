package es.dlj.onlinestore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{
}
