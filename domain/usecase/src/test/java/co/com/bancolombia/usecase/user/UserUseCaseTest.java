package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.role.Role;
import co.com.bancolombia.model.role.gateways.RoleRepository;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    UserUseCase useCase;

    private static final Role ROLE_ADMIN = Role.builder()
            .id(1L).name("ADMIN").description("Administrador").build();

    private static final User USER_WALTER = User.builder()
            .id(10L).name("Walter").lastName("Molina")
            .documentNumber("73727173").birthDate(LocalDate.of(1992, 2, 2))
            .address("Calle Ayacucho").cellphone("999888777")
            .email("walter@gmail.com").salary(new BigDecimal("2500.00"))
            .roleId(1L)
            .build();

    private static final User USER_TO_SAVE = User.builder()
            .name("Moises").lastName("Molina")
            .documentNumber("12345678").birthDate(LocalDate.of(1999, 5, 22))
            .address("Av. Puquina 115").cellphone("988776655")
            .email("moises@gmail.com").salary(new BigDecimal("1800.00"))
            .roleId(1L)
            .build();

    private static final User USER_SAVED = USER_TO_SAVE.toBuilder()
            .id(11L).build();

    @BeforeEach
    void setUp() {
        useCase = new UserUseCase(userRepository, roleRepository);
    }

    @Test
    void shouldSaveUser_validRole() {
        when(roleRepository.findRoleBydId(1L)).thenReturn(Mono.just(ROLE_ADMIN));
        when(userRepository.saveUser(USER_TO_SAVE)).thenReturn(Mono.just(USER_SAVED));
        StepVerifier.create(useCase.saveUser(USER_TO_SAVE))
                .expectNext(USER_SAVED)
                .verifyComplete();
    }

    @Test
    void shouldUpdateUser() {
        var updated = USER_WALTER.toBuilder().address("Nueva dirección").build();
        when(userRepository.updateUser(updated)).thenReturn(Mono.just(updated));
        StepVerifier.create(useCase.updateUser(updated))
                .expectNext(updated)
                .verifyComplete();
    }

    @Test
    void shouldFindUserById() {
        when(userRepository.findUserById(10L)).thenReturn(Mono.just(USER_WALTER));
        StepVerifier.create(useCase.findUserById(10L))
                .expectNext(USER_WALTER)
                .verifyComplete();
    }

    @Test
    void shouldFindUserByDocumentNumber() {
        when(userRepository.findUserByDocumentNumber("73727173")).thenReturn(Mono.just(USER_WALTER));
        StepVerifier.create(useCase.findUserByDocumentNumber("73727173"))
                .expectNext(USER_WALTER)
                .verifyComplete();
    }

    @Test
    void shouldFindUserByEmail() {
        when(userRepository.findUserByEmail("walter@gmail.com")).thenReturn(Mono.just(USER_WALTER));
        StepVerifier.create(useCase.findUserByEmail("walter@gmail.com"))
                .expectNext(USER_WALTER)
                .verifyComplete();
    }

    @Test
    void shouldFindAllUsers() {
        when(userRepository.findAllUsers()).thenReturn(Flux.just(USER_WALTER, USER_SAVED));
        StepVerifier.create(useCase.findAllUsers())
                .expectNext(USER_WALTER)
                .expectNext(USER_SAVED)
                .verifyComplete();
    }

    @Test
    void shouldDeleteByEmail() {
        when(userRepository.deleteByEmail("walter@gmail.com")).thenReturn(Mono.empty());
        StepVerifier.create(useCase.deleteByEmail("walter@gmail.com"))
                .verifyComplete();
    }

    @Test
    void shouldSaveUserEvenIfRoleIsEmpty_withCurrentImplementation() {
        when(roleRepository.findRoleBydId(1L)).thenReturn(Mono.empty());
        when(userRepository.saveUser(USER_TO_SAVE)).thenReturn(Mono.just(USER_SAVED));
        StepVerifier.create(useCase.saveUser(USER_TO_SAVE))
                .expectNext(USER_SAVED)
                .verifyComplete();
    }
}
