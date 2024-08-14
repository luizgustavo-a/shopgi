package tech.shopgi.customerms.dto;

import tech.shopgi.customerms.model.Customer;

public record PublicCustomerDto (
        String id,
        String name,
        String document,
        String dateOfBirth,
        String gender,
        AddressDto address,
        String email,
        String contactNumber
){
    public PublicCustomerDto (Customer customer) {
        this(
                String.valueOf(customer.getId()),
                customer.getName(),
                customer.getDocument(),
                customer.getDateOfBirth(),
                customer.getGender().toString(),
                new AddressDto(customer.getAddress()),
                customer.getEmail(),
                customer.getContactNumber()
        );
    }
}
