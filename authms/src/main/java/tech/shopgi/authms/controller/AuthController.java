package tech.shopgi.authms.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import tech.shopgi.authms.dto.JwtTokenDataDto;
import tech.shopgi.authms.dto.LoginRequestDto;
import tech.shopgi.authms.dto.RegisterRequestDto;
import tech.shopgi.authms.dto.UpdateRequestDto;
import tech.shopgi.authms.model.User;
import tech.shopgi.authms.model.exception.InvalidUserInformationException;
import tech.shopgi.authms.service.AuthService;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager manager;
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDataDto> login(LoginRequestDto loginRequestDto) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password());
        var authentication = manager.authenticate(usernamePasswordAuthenticationToken);
        var token = service.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new JwtTokenDataDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtTokenDataDto> register(RegisterRequestDto registerRequestDto) throws InvalidUserInformationException {
        User registeredUser = service.register(registerRequestDto);
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

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String token,
                                    @RequestBody UpdateRequestDto updateRequestDto) throws InvalidUserInformationException {
        String jwtToken = token.substring(7);
        if (service.validateToken(jwtToken)) {
            String username = service.getUsernameFromToken(jwtToken);
            var updatedUser = service.update(updateRequestDto, username);

            return ResponseEntity.ok("User " + updatedUser.getUsername() + " successfully updated.");
        }
        else throw new InvalidUserInformationException("Invalid update information.");
    }
}
