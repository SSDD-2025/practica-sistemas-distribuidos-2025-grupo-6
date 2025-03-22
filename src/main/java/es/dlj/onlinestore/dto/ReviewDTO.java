package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;

public record ReviewDTO(
    Long id,
    LocalDateTime creationDate,
    String title,
    String description,
    int rating,
    UserSimpleDTO user,
    ProductSimpleDTO product
) {}
