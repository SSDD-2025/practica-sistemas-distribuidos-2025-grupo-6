package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;
import java.util.List;

import es.dlj.onlinestore.enumeration.PaymentMethod;
import jakarta.validation.constraints.Size;

public record UserSimpleDTO(
    Long id,
    LocalDateTime creationDate,

    ImageDTO profilePhoto,

    String name,
    String surname,
    String username,
    String email,
    String address,
    String city,
    String postalCode,
    String phone,
    
    PaymentMethod paymentMethod,
    List<String> roles
) {}
