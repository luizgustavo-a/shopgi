package tech.shopgi.customerms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateCustomerDto(
        @NotBlank
        String name,
        @NotBlank
        @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$")
        String document,
        @NotBlank
        @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$")
        String dateOfBirth,
        @NotBlank
        String gender,
        AddressDto address,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Pattern(regexp = "^\\+?\\d{0,3}[-.\\s]?(\\(?\\d{2,3}\\)?[-.\\s]?)?\\d{4,5}[-.\\s]?\\d{4}$")
        String contactNumber
) {
}
