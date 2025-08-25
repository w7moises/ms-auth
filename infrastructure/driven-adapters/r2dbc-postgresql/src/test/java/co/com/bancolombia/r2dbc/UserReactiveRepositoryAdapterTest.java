package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import co.com.bancolombia.r2dbc.exception.FoundException;
import co.com.bancolombia.r2dbc.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @InjectMocks
    UserReactiveRepositoryAdapter adapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    public static final User USER_MOCK = User.builder()
            .id(1L)
            .name("Moises")
            .lastName("Molina")
            .documentNumber("73727173")
            .birthDate(LocalDate.of(1992, 2, 2))
            .address("Calle ayacucho")
            .cellphone("982389811")
            .email("w7@gmail.com")
            .salary(new BigDecimal("2345"))
            .build();

    public static final User USER_MOCK2 = User.builder()
            .id(2L)
            .name("Walter")
            .lastName("Molina")
            .documentNumber("73727173")
            .birthDate(LocalDate.of(1999, 5, 22))
            .address("Av. puquina 115")
            .cellphone("666423665")
            .email("wmolina@gmail.com")
            .salary(new BigDecimal("1234"))
            .build();

    public static final User USER_MOCK_UPDATE = User.builder()
            .id(2L)
            .name("Walter")
            .lastName("Molina Update")
            .documentNumber("73727173")
            .birthDate(LocalDate.of(1999, 5, 22))
            .address("Av. puquina 115")
            .cellphone("666423665")
            .email("wmolina@gmail.com")
            .salary(new BigDecimal("1234"))
            .build();

    public static final UserEntity USER_MOCK_ENTITY = UserEntity.builder()
            .id(2L)
            .name("Walter")
            .lastName("Molina")
            .documentNumber("73727173")
            .birthDate(LocalDate.of(1999, 5, 22))
            .address("Av. puquina 115")
            .cellphone("666423665")
            .email("wmolina@gmail.com")
            .salary(new BigDecimal("1234"))
            .build();

    public static final UserEntity USER_MOCK_ENTITY2 = UserEntity.builder()
            .id(9L)
            .name("Walter")
            .lastName("Molina")
            .documentNumber("73727173")
            .birthDate(LocalDate.of(1999, 5, 22))
            .address("Av. puquina 115")
            .cellphone("666423665")
            .email("wmolina@gmail.com")
            .salary(new BigDecimal("1234"))
            .build();

    @Test
    void shouldFindUserById() {
        when(mapper.map(USER_MOCK_ENTITY, User.class)).thenReturn(USER_MOCK);
        when(repository.findById(1L)).thenReturn(Mono.just(USER_MOCK_ENTITY));
        Mono<User> data = adapter.findById(1L);
        StepVerifier.create(data)
                .expectNextMatches(user -> user.getId().equals(1L) && user.getName().equals("Moises"))
                .verifyComplete();
    }

    @Test
    void shouldFindUserByEmail() {
        when(mapper.map(USER_MOCK_ENTITY, User.class)).thenReturn(USER_MOCK);
        when(repository.findByEmail("wmolina@gmail.com")).thenReturn(Mono.just(USER_MOCK_ENTITY));
        Mono<User> data = adapter.findUserByEmail("wmolina@gmail.com");
        StepVerifier.create(data)
                .expectNextMatches(user -> user.getId().equals(1L) && user.getName().equals("Moises"))
                .verifyComplete();
    }

    @Test
    void shouldFindUserByDocumentNumber() {
        when(mapper.map(USER_MOCK_ENTITY, User.class)).thenReturn(USER_MOCK);
        when(repository.findByDocumentNumber("73727173")).thenReturn(Mono.just(USER_MOCK_ENTITY));
        Mono<User> data = adapter.findUserByDocumentNumber("73727173");
        StepVerifier.create(data)
                .expectNextMatches(user -> user.getId().equals(1L) && user.getName().equals("Moises"))
                .verifyComplete();
    }

    @Test
    void shouldFindAllUsers() {
        when(mapper.map(USER_MOCK_ENTITY, User.class)).thenReturn(USER_MOCK, USER_MOCK2);
        when(repository.findAll()).thenReturn(Flux.just(USER_MOCK_ENTITY));
        Flux<User> data = adapter.findAll();
        StepVerifier.create(data)
                .expectNext(USER_MOCK)
                .verifyComplete();
    }

    @Test
    void shouldSaveUserWhenEmailDoesNotExist() {
        when(repository.existsByEmail(USER_MOCK.getEmail())).thenReturn(Mono.just(false));
        when(mapper.map(USER_MOCK, UserEntity.class)).thenReturn(USER_MOCK_ENTITY);
        when(repository.save(USER_MOCK_ENTITY)).thenReturn(Mono.just(USER_MOCK_ENTITY));
        when(mapper.map(USER_MOCK_ENTITY, User.class)).thenReturn(USER_MOCK);
        StepVerifier.create(adapter.saveUser(USER_MOCK))
                .expectNext(USER_MOCK)
                .verifyComplete();
    }

    @Test
    void shouldNotSaveUserWhenEmailExist() {
        when(repository.existsByEmail(USER_MOCK.getEmail())).thenReturn(Mono.just(true));
        StepVerifier.create(adapter.saveUser(USER_MOCK))
                .expectError(FoundException.class)
                .verify();
    }

    @Test
    void shouldNotUpdateUserIfIdDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Mono.empty());
        StepVerifier.create(adapter.updateUser(USER_MOCK))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void shouldNotUpdateUserIfEmailExist() {
        when(repository.findById(2L)).thenReturn(Mono.just(USER_MOCK_ENTITY));
        when(mapper.map(USER_MOCK_ENTITY, User.class)).thenReturn(USER_MOCK);
        when(repository.findByEmail("wmolina@gmail.com"))
                .thenReturn(Mono.just(USER_MOCK_ENTITY2));
        StepVerifier.create(adapter.updateUser(USER_MOCK_UPDATE))
                .expectError(FoundException.class)
                .verify();
    }

    @Test
    void shouldUpdateUserIfEmailNotExist() {
        when(repository.findById(1L)).thenReturn(Mono.just(USER_MOCK_ENTITY));
        when(mapper.map(USER_MOCK_ENTITY, User.class)).thenReturn(USER_MOCK);
        when(repository.findByEmail("w7@gmail.com")).thenReturn(Mono.empty());
        when(mapper.map(USER_MOCK, UserEntity.class)).thenReturn(USER_MOCK_ENTITY);
        when(repository.save(USER_MOCK_ENTITY)).thenReturn(Mono.just(USER_MOCK_ENTITY));
        StepVerifier.create(adapter.updateUser(USER_MOCK))
                .expectNext(USER_MOCK)
                .verifyComplete();
    }

    @Test
    void shouldDeleteUserByEmail() {
        String email = USER_MOCK2.getEmail();
        when(repository.findByEmail(email)).thenReturn(Mono.just(USER_MOCK_ENTITY));
        when(repository.deleteById(USER_MOCK_ENTITY.getId())).thenReturn(Mono.empty());
        StepVerifier.create(adapter.deleteByEmail(email))
                .verifyComplete();
    }
}
