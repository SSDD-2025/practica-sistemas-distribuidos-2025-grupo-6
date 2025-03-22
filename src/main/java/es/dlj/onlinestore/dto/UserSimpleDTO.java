package es.dlj.onlinestore.dto;

import java.util.List;

import es.dlj.onlinestore.enumeration.PaymentMethod;

public record UserSimpleDTO(
    Long id,
    String name,
    String surname,
    String username,
    String email,
    String address,
    String city,
    String postalCode,
    String phone,
    PaymentMethod paymentMethod,
    ImageDTO profilePhoto,
    List<String> roles
) {}
