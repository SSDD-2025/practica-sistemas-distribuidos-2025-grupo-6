package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
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
) {
    public String getCreationDateFormattedSimpler() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
        return creationDate.format(formatter);
    }

    public String getCreationDateFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'of' MMMM, yyyy 'at' HH:mm", Locale.ENGLISH);
        return creationDate.format(formatter);
    }
}
