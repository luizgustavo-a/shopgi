package tech.shopgi.customerms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import tech.shopgi.customerms.model.Address;

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
    public AddressDto(Address address) {
        this(
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getCity(),
                address.getState(),
                address.getPostalCode()
        );
    }
}
