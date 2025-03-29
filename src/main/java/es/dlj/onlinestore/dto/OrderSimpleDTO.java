package es.dlj.onlinestore.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record OrderSimpleDTO(
    Long id,
    LocalDateTime creationDate,
    float totalPrice,
    String address,
    String phoneNumber
) {
    public String getCreationDateFormattedSimpler() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
        return creationDate.format(formatter);
    }
}
