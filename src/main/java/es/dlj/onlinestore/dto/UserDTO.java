package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public List<Map<String, Object>> getPaymentMethodsMapped() {
        List<Map<String, Object>> paymentMethods = new ArrayList<>();
        for (PaymentMethod pMethod : PaymentMethod.values()) {
            paymentMethods.add(Map.of("name", pMethod.toString(), "selected", (paymentMethod != null && paymentMethod.equals(pMethod))));
        }
        return paymentMethods;
    }
}
