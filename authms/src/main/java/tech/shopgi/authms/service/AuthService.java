package tech.shopgi.authms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.shopgi.authms.dto.LoginRequestDto;
import tech.shopgi.authms.dto.RegisterRequestDto;
import tech.shopgi.authms.model.User;
import tech.shopgi.authms.model.exception.InvalidUserInformationException;
import tech.shopgi.authms.repository.UserRepository;
import tech.shopgi.authms.security.JwtTokenProvider;

import java.util.List;
import java.util.Optional;

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

    public User register(RegisterRequestDto registerRequestDto) {
        User user = new User();
        user.setUsername(registerRequestDto.username());
        user.setPassword(passwordEncoder.encode(registerRequestDto.password()));
        user.setRoles(List.of("ROLE_USER"));

        if(!registerRequestDto.roles().isEmpty()) {
            for(String role: registerRequestDto.roles()) {
                if (!role.equals("ROLE_USER")) {
                    user.getRoles().add(role);
                }
            }
        }

        userRepository.save(user);

        return user;
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
