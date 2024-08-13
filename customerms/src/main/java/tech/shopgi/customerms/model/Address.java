package tech.shopgi.customerms.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Address {
    private String street;
    private String number;
    private String complement;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
