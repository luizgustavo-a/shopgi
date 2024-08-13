package tech.shopgi.authms.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.shopgi.authms.dto.LoginRequestDto;
import tech.shopgi.authms.dto.RegisterRequestDto;
import tech.shopgi.authms.dto.UpdateRequestDto;
import tech.shopgi.authms.model.User;
import tech.shopgi.authms.model.UserRoles;
import tech.shopgi.authms.model.exception.InvalidUserInformationException;
import tech.shopgi.authms.repository.UserRepository;
import tech.shopgi.authms.security.JwtTokenProvider;

import java.util.ArrayList;
import java.util.Arrays;

@AllArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager manager;

    public String login(LoginRequestDto loginRequestDto) throws AuthenticationException {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password());
        var authentication = manager.authenticate(usernamePasswordAuthenticationToken);
        return generateToken((User) authentication.getPrincipal());
    }

    public User register(RegisterRequestDto registerRequestDto) throws InvalidUserInformationException {
        User user = new User();
        user.setUsername(registerRequestDto.username());
        user.setPassword(passwordEncoder.encode(registerRequestDto.password()));
        user.setRoles(new ArrayList<>());
        user.getRoles().add("ROLE_USER");

        if (registerRequestDto.roles() != null && !registerRequestDto.roles().isEmpty()) {
            for (String role : registerRequestDto.roles()) {
                if (!role.equals("ROLE_USER")){
                    if (Arrays.stream(UserRoles.values()).anyMatch(r -> r.toString().equalsIgnoreCase(role))) {
                        user.getRoles().add(role);
                    } else throw new InvalidUserInformationException("Invalid role.");
                }
            }
        }

        return userRepository.save(user);
    }

    public String generateToken(User user) {
        return jwtTokenProvider.generateToken(user);
    }

    public String getUsernameFromToken(String token) {
        return jwtTokenProvider.getUsernameFromToken(token);
    }

    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    public User loadUserByUsername(String username) throws InvalidUserInformationException {
        var user = (User) userRepository.findByUsername(username);
        if(user != null) {
            return user;
        } else throw new InvalidUserInformationException("Username not found.");
    }

    public User update(UpdateRequestDto updateRequestDto, String username) throws InvalidUserInformationException {
        User user = loadUserByUsername(username);

        if(updateRequestDto.newUsername() != null && !updateRequestDto.newUsername().isEmpty()) {
            user.setUsername(updateRequestDto.newUsername());
        }
        if(updateRequestDto.newPassword() != null && !updateRequestDto.newPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateRequestDto.newPassword()));
        }

        return userRepository.save(user);
    }
}
