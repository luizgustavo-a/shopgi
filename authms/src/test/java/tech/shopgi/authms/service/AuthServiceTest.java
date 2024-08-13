package tech.shopgi.authms.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import tech.shopgi.authms.dto.RegisterRequestDto;
import tech.shopgi.authms.dto.UpdateRequestDto;
import tech.shopgi.authms.model.User;
import tech.shopgi.authms.model.exception.InvalidUserInformationException;
import tech.shopgi.authms.repository.UserRepository;
import tech.shopgi.authms.security.JwtTokenProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    public void AuthService_Register_ReturnRegisteredUser() throws InvalidUserInformationException {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("username", "password", List.of("ROLE_USER", "ROLE_ADMIN"));
        User expectedUser = new User("username", "password", List.of("ROLE_USER", "ROLE_ADMIN"));

        when(userRepository.save(Mockito.any(User.class))).thenReturn(expectedUser);
        var registeredUser = authService.register(registerRequestDto);

        Assertions.assertEquals(expectedUser.getUsername(), registeredUser.getUsername());
        Assertions.assertEquals(expectedUser.getPassword(), registeredUser.getPassword());
        Assertions.assertEquals(expectedUser.getRoles(), registeredUser.getRoles());
        Assertions.assertEquals(expectedUser.getAuthorities(), registeredUser.getAuthorities());
    }

    @Test
    public void AuthService_Register_ThrowInvalidUserInformationException() {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("username", "password", List.of("ROLE_ERROR"));

       Assertions.assertThrows(InvalidUserInformationException.class, () -> authService.register(registerRequestDto));
    }

    @Test
    public void AuthService_GenerateToke_ReturnToken() {
        User user = new User();
        String expectedToken = "token";

        when(jwtTokenProvider.generateToken(Mockito.any(User.class))).thenReturn(expectedToken);
        var token = authService.generateToken(user);

        Assertions.assertEquals(expectedToken, token);
    }

    @Test
    public void AuthService_GenerateToke_ReturnTrueTokenValid() {
        String token = "token";

        when(jwtTokenProvider.validateToken(token)).thenReturn(true);

        Assertions.assertTrue(authService.validateToken(token));
    }

    @Test
    public void AuthService_GenerateToke_ReturnFalseTokenInvalid() {
        String token = "token";

        when(jwtTokenProvider.validateToken(token)).thenReturn(false);

        Assertions.assertFalse(authService.validateToken(token));
    }

    @Test
    public void AuthService_Update_ReturnUpdatedUser() throws InvalidUserInformationException {
        UpdateRequestDto updateRequestDto = new UpdateRequestDto("newUsername", "newPassword");
        User user = new User("username", "password", List.of("ROLE_USER", "ROLE_ADMIN"));
        User expectedUpdatedUser = new User("newUsername", "encryptedPassword", List.of("ROLE_USER", "ROLE_ADMIN"));

        when(authService.loadUserByUsername("username")).thenReturn(user);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(expectedUpdatedUser);
        when(passwordEncoder.encode("newPassword")).thenReturn("encryptedPassword");

        var updatedUser = authService.update(updateRequestDto, "username");
        var encryptedPassword = passwordEncoder.encode("newPassword");

        Assertions.assertEquals(expectedUpdatedUser.getUsername(), updatedUser.getUsername());
        Assertions.assertEquals(encryptedPassword, updatedUser.getPassword());
    }

}