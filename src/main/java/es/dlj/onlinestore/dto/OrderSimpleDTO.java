package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;

public record OrderSimpleDTO(
    Long id,
    LocalDateTime creationDate,
    float totalPrice,
    String address,
    String phoneNumber) {}
