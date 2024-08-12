package tech.shopgi.authms.dto;

import jakarta.validation.constraints.Pattern;

import java.util.List;

public record RegisterRequestDto(
        @Pattern(regexp = "^[\\w]{4,20}$", message = "Username must be 4 to 20 characters long, with no spaces.")
        String username,
        @Pattern(regexp = "^.{4,}$", message = "Password must be at least 4 characters long.")
        String password,
        List<String> roles
) {
}
