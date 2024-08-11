package tech.shopgi.authms.dto;

import java.util.List;

public record RegisterRequestDto(
        String username,
        String password,
        List<String> roles
) {
}
