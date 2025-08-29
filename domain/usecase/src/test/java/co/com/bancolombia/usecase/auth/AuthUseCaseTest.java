package co.com.bancolombia.usecase.auth;

import co.com.bancolombia.model.login.LoginResponse;
import co.com.bancolombia.model.login.gateways.PasswordHasher;
import co.com.bancolombia.model.login.gateways.TokenGenerator;
import co.com.bancolombia.model.role.Role;
import co.com.bancolombia.model.role.gateways.RoleRepository;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private TokenGenerator tokenGenerator;

    private AuthUseCase useCase;

    private static final User USER = User.builder()
            .id(1L)
            .email("w7moises@gmail.com")
            .password("hashedPassword")
            .roleId(1L)
            .build();

    private static final Role ROLE = Role.builder()
            .id(1L)
            .name("ADMIN")
            .description("Administrator")
            .build();

    private static final LoginResponse LOGIN_RESPONSE = new LoginResponse("jwt-token-123");

    @BeforeEach
    void setUp() {
        useCase = new AuthUseCase(userRepository, roleRepository, passwordHasher, tokenGenerator);
    }

    @Test
    void shouldLoginSuccessfully() {
        when(userRepository.findUserByEmail("w7moises@gmail.com")).thenReturn(Mono.just(USER));
        when(passwordHasher.verifyPassword(USER, "password123")).thenReturn(Mono.just(USER));
        when(roleRepository.findRoleBydId(1L)).thenReturn(Mono.just(ROLE));
        when(tokenGenerator.generate("w7moises@gmail.com", List.of("ADMIN"), 1L))
                .thenReturn("jwt-token-123");
        StepVerifier.create(useCase.login("w7moises@gmail.com", "password123"))
                .expectNext(LOGIN_RESPONSE)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenPasswordIsInvalid() {
        when(userRepository.findUserByEmail("w7moises@gmail.com")).thenReturn(Mono.just(USER));
        when(passwordHasher.verifyPassword(USER, "error505"))
                .thenReturn(Mono.error(new RuntimeException("Invalid Credentials")));
        StepVerifier.create(useCase.login("w7moises@gmail.com", "error505"))
                .expectErrorMatches(ex -> ex.getMessage().equals("Invalid Credentials"))
                .verify();
    }
}
