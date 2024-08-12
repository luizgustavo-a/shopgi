package tech.shopgi.authms.dto;

import jakarta.validation.constraints.Pattern;

public record UpdateRequestDto(
        @Pattern(regexp = "^[\\w]{4,20}$", message = "Username must be 4 to 20 characters long, with no spaces.")
        String newUsername,
        @Pattern(regexp = "^.{4,}$", message = "Password must be at least 4 characters long.")
        String newPassword
) {
}
