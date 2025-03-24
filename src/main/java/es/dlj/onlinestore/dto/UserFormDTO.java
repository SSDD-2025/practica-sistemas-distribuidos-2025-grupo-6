package es.dlj.onlinestore.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserFormDTO(

    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    String name,
    
    @Size(min = 3, max = 100, message = "Surname must be between 3 and 100 characters")
    String surname,

    @Size(min = 3, max = 100, message = "User name must be between 3 and 100 characters")
    String username,

    @Size(min = 6, max = 200, message = "Email must be between 6 and 200 characters")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email is not valid, it shoudl be like: example@domain.com")
    String email,

    @Size(max = 200, message = "Address must be between 3 and 200 characters")
    String address,

    @Size(max = 100, message = "City must be between 3 and 100 characters")
    String city,

    @Pattern(regexp = "^$|^[0-9]{5}$", message = "Postal code must have 5 numbers, it should be like: 12345")
    String postalCode,

    @Pattern(regexp = "^$|^\\+?[0-9]{1,3}?[0-9]{9,15}$", message = "Phone number is not valid, it should be like: +34123456789 or 123456789")
    String phone,

    @Size(min = 8, message = "Password must have at least 8 characters")
    String password,

    @Size(min = 8, message = "Password must have at least 8 characters")
    String repeatedPassword
) {}
