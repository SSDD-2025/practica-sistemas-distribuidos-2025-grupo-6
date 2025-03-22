package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import es.dlj.onlinestore.enumeration.PaymentMethod;


public record OrderDTO(
    Long id,
    LocalDateTime creationDate,
    UserSimpleDTO user,
    Set<ProductSimpleDTO> products,
    List<String> nonContinuedProducts,
    float totalPrice,
    PaymentMethod paymentMethod,
    String address,
    String phoneNumber
) {}
