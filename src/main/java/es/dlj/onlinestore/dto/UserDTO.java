package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import es.dlj.onlinestore.enumeration.PaymentMethod;

public record UserDTO(
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
    List<String> roles,

    List<ProductSimpleDTO> productsForSell,
    List<ReviewDTO> reviews,
    List<OrderSimpleDTO> orders,
    Set<ProductSimpleDTO> cartProducts
) {
    public float getCartTotalPrice() {
        return (float) cartProducts.stream().mapToDouble(ProductSimpleDTO::getPriceWithSale).sum();
    }
}
