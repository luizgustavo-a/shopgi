package tech.shopgi.authms.controller;

import io.jsonwebtoken.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.shopgi.authms.dto.JwtTokenDataDto;
import tech.shopgi.authms.dto.LoginRequestDto;
import tech.shopgi.authms.dto.RegisterRequestDto;
import tech.shopgi.authms.model.User;
import tech.shopgi.authms.service.AuthService;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager manager;
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDataDto> login(LoginRequestDto dto) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        var authentication = manager.authenticate(usernamePasswordAuthenticationToken);
        var token = service.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new JwtTokenDataDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtTokenDataDto> register(RegisterRequestDto dto) {
        User registeredUser = service.register(dto);
        var token = service.generateToken(registeredUser);
        return ResponseEntity.ok(new JwtTokenDataDto(token));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        if (service.validateToken(jwtToken)) {
            String username = service.getUsernameFromToken(jwtToken);
            var user = service.loadUserByUsername(username);
            var newToken = service.generateToken(user);

            return ResponseEntity.ok(new JwtTokenDataDto(newToken));
        }
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    }
}
