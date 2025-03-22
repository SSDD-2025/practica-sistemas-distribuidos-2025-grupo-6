package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;
import java.util.List;

import es.dlj.onlinestore.enumeration.ProductType;

public record ProductDTO(
    Long id,
    LocalDateTime creationDate,
    String name,
    String description,
    float price,
    int stock,
    float sale,
    List<ImageDTO> images,
    List<ReviewSimpleDTO> reviews,
    UserSimpleDTO seller,
    List<ProductTagSimpleDTO> tags,
    ProductType productType,
    int totalSells
) {}
