package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;

import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.enumeration.PaymentMethod;
import es.dlj.onlinestore.domain.Product;


public record OrderDTO(
    Long id,
    LocalDateTime creationDate,
    User user,
    Set<Product> products,
    List<String>nonContinuedProducts,
    float totalPrice,
    PaymentMethod paymentMethod,
    String address,
    String phoneNumber
) {}
