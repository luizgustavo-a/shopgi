package tech.shopgi.authms.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.shopgi.authms.dto.LoginRequestDto;
import tech.shopgi.authms.dto.RegisterRequestDto;
import tech.shopgi.authms.dto.UpdateRequestDto;
import tech.shopgi.authms.model.User;
import tech.shopgi.authms.model.exception.InvalidUserInformationException;
import tech.shopgi.authms.repository.UserRepository;
import tech.shopgi.authms.security.JwtTokenProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager manager;

    @InjectMocks
    private AuthService authService;

    @Test
    public void AuthService_Login_ReturnToken() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("username", "password");
        User user = new User("username", "password", List.of("ROLE_USER"));
        var authToken = new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password());
        var authentication = mock(Authentication.class);
        String expectedToken = "token";

        when(manager.authenticate(authToken)).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authService.generateToken(user)).thenReturn(expectedToken);

        var token = authService.login(loginRequestDto);

        Assertions.assertEquals(expectedToken, token);
        verify(manager).authenticate(authToken);
    }

    @Test
    public void AuthService_Login_ThrowAuthenticationException() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("invalidUsername", "invalidPassword");
        var authToken = new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password());

        when(manager.authenticate(authToken)).thenThrow(new BadCredentialsException("Invalid username or password."));

        assertThrows(AuthenticationException.class, () -> authService.login(loginRequestDto));
        verify(manager).authenticate(authToken);
    }

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

        when(userRepository.findByUsername("username")).thenReturn(user);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(expectedUpdatedUser);
        when(passwordEncoder.encode("newPassword")).thenReturn("encryptedPassword");

        var updatedUser = authService.update(updateRequestDto, "username");
        var encryptedPassword = passwordEncoder.encode("newPassword");

        Assertions.assertEquals(expectedUpdatedUser.getUsername(), updatedUser.getUsername());
        Assertions.assertEquals(encryptedPassword, updatedUser.getPassword());
    }

}