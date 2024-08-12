package tech.shopgi.authms.dto;

public record UpdateRequestDto(
        String newUsername,
        String newPassword
) {
}
