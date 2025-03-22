package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;

public record ReviewSimpleDTO(
    Long id,
    LocalDateTime creationDate,
    String title,
    String description,
    int rating
) {}
