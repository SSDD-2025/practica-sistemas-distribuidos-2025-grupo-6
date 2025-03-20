package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;
import java.util.List;

import es.dlj.onlinestore.domain.Review;
import es.dlj.onlinestore.domain.Image;
import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.domain.ProductTag;
import es.dlj.onlinestore.enumeration.ProductType;

public record ProductDTO(
    Long id,
    LocalDateTime creationDate,
    String name,
    String description,
    float price,
    int stock,
    float sale,
    List<Image> images,
    List<Review> reviews,
    User seller,
    List<ProductTag> tags,
    ProductType productType,
    int totalSells
) {}
