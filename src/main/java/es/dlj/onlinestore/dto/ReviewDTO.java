package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.Size;

public record ReviewDTO(
    Long id,
    LocalDateTime creationDate,

    @Size(min = 3, max = 100, message = "Description must be between 3 and 100 characters")	
    String title,

    @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters")	
    String description,

    @Range(min = 0, max = 5, message = "Rating must be between 0 and 5")
    int rating,
    
    UserSimpleDTO user,
    ProductSimpleDTO product
) {}
