package tech.shopgi.customerms.dto;

import jakarta.validation.constraints.Pattern;

public record UpdateCustomerDto(
        Long id,
        String name,
        @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$")
        String document,
        @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])([-/])(0[1-9]|1[0-2])\\2(\\d{4})$")
        String dateOfBirth,
        String gender,
        AddressDto address,
        String email,
        @Pattern(regexp = "^\\+?\\d{0,3}[-.\\s]?(\\(?\\d{2,3}\\)?[-.\\s]?)?\\d{4,5}[-.\\s]?\\d{4}$")
        String contactNumber
) {
}
