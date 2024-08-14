package tech.shopgi.customerms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.NoArgsConstructor;

public record AddressDto(
        @NotBlank
        String street,
        @NotBlank
        String number,
        String complement,
        @NotBlank
        String city,
        @NotBlank
        String state,
        @NotBlank
        @Pattern(regexp = "^\\d{5}-?\\d{3}$")
        String postalCode
) {
}
