package tech.shopgi.authms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.shopgi.authms.dto.LoginRequestDto;
import tech.shopgi.authms.dto.RegisterRequestDto;
import tech.shopgi.authms.model.User;
import tech.shopgi.authms.model.UserRoles;
import tech.shopgi.authms.model.exception.InvalidUserInformationException;
import tech.shopgi.authms.repository.UserRepository;
import tech.shopgi.authms.security.JwtTokenProvider;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String authenticate(LoginRequestDto loginRequestDto) throws InvalidUserInformationException {
        UserDetails userDetails = userRepository.findByUsername(loginRequestDto.username());

        if (userDetails == null) {
            throw new InvalidUserInformationException("User not found");
        }

        if (passwordEncoder.matches(loginRequestDto.password(), userDetails.getPassword())) {
            return jwtTokenProvider.generateToken((User) userDetails);
        }

        throw new InvalidUserInformationException("Invalid password");
    }

    public User register(RegisterRequestDto registerRequestDto) throws InvalidUserInformationException {
        User user = new User();
        user.setUsername(registerRequestDto.username());
        user.setPassword(passwordEncoder.encode(registerRequestDto.password()));
        user.setRoles(new ArrayList<>());
        user.getRoles().add("ROLE_USER");

        if (registerRequestDto.roles() != null && !registerRequestDto.roles().isEmpty()) {
            for (String role : registerRequestDto.roles()) {
                if (!role.equals("ROLE_USER") && Arrays.stream(UserRoles.values()).anyMatch(r -> r.toString().equalsIgnoreCase(role))) {
                    user.getRoles().add(role);
                } else throw new InvalidUserInformationException("Invalid role.");
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

    public User loadUserByUsername(String username) {
        return (User) userRepository.findByUsername(username);
    }
}
