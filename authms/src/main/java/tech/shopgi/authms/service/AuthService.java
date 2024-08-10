package tech.shopgi.authms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.shopgi.authms.dto.LoginRequestDto;
import tech.shopgi.authms.dto.RegisterRequestDto;
import tech.shopgi.authms.model.User;
import tech.shopgi.authms.model.exception.InvalidUserInformationException;
import tech.shopgi.authms.repository.UserRepository;
import tech.shopgi.authms.security.JwtTokenProvider;

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
        Optional<User> optionalUser = userRepository.findByUsername(loginRequestDto.username());
        if (optionalUser.isPresent() && passwordEncoder.matches(loginRequestDto.password(), optionalUser.get().getPassword())) {
            return jwtTokenProvider.generateToken(optionalUser.get());
        }
        throw new InvalidUserInformationException();
    }

    public void register(RegisterRequestDto registerRequestDto) {
        User user = new User(
                registerRequestDto.username(),
                passwordEncoder.encode(registerRequestDto.password()),
                "ROLE_USER"
        );

        userRepository.save(user);
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
}
