package es.dlj.onlinestore.dto;

public record OrderFormtDTO (
    String paymentMethod,
    String address,
    String phoneNumber
) {}
