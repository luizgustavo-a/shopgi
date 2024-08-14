package tech.shopgi.customerms.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Gender {
    MALE(Arrays.asList("male", "masculino", "m")),
    FEMALE(Arrays.asList("female", "feminino", "f")),
    NON_BINARY(Arrays.asList("non-binary", "não-binário", "nb"));

    private final List<String> genders;

    Gender(List<String> genders) {
        this.genders = genders;
    }

    public static Gender fromString(String input) {
        for (Gender gender : Gender.values()) {
            if (gender.getGenders().stream().anyMatch(g -> g.equalsIgnoreCase(input))) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender input.");
    }

}
