package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.role.Role;
import co.com.bancolombia.r2dbc.entity.RoleEntity;
import co.com.bancolombia.r2dbc.exception.NotFoundException;
import co.com.bancolombia.r2dbc.repository.RoleReactiveRepository;
import co.com.bancolombia.r2dbc.repository.RoleReactiveRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleReactiveRepositoryAdapterTest {

    @InjectMocks
    RoleReactiveRepositoryAdapter adapter;

    @Mock
    RoleReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private static final Role ROLE_MOCK = Role.builder()
            .id(1L)
            .name("ADMIN")
            .description("Administrador del sistema")
            .build();

    private static final RoleEntity ROLE_ENTITY_MOCK = RoleEntity.builder()
            .id(1L)
            .name("ADMIN")
            .description("Administrador del sistema")
            .build();

    private static final RoleEntity ROLE_ENTITY_SAVED = RoleEntity.builder()
            .id(5L)
            .name("AUDITOR")
            .description("Rol de auditoría")
            .build();

    private static final Role ROLE_TO_SAVE = Role.builder()
            .name("AUDITOR")
            .description("Rol de auditoría")
            .build();

    private static final Role ROLE_SAVED = Role.builder()
            .id(5L)
            .name("AUDITOR")
            .description("Rol de auditoría")
            .build();

    @Test
    void shouldFindRoleById() {
        when(repository.findById(1L)).thenReturn(Mono.just(ROLE_ENTITY_MOCK));
        when(mapper.map(ROLE_ENTITY_MOCK, Role.class)).thenReturn(ROLE_MOCK);
        StepVerifier.create(adapter.findRoleBydId(1L))
                .expectNextMatches(r -> r.getId().equals(1L) && r.getName().equals("ADMIN"))
                .verifyComplete();
    }

    @Test
    void shouldReturnNotFoundWhenRoleDoesNotExist() {
        when(repository.findById(9L)).thenReturn(Mono.empty());
        StepVerifier.create(adapter.findRoleBydId(9L))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void shouldSaveRole() {
        when(mapper.map(ROLE_TO_SAVE, RoleEntity.class)).thenReturn(ROLE_ENTITY_SAVED);
        when(repository.save(any(RoleEntity.class))).thenReturn(Mono.just(ROLE_ENTITY_SAVED));
        when(mapper.map(ROLE_ENTITY_SAVED, Role.class)).thenReturn(ROLE_SAVED);
        StepVerifier.create(adapter.saveRole(ROLE_TO_SAVE))
                .expectNextMatches(r -> r.getId().equals(5L) && r.getName().equals("AUDITOR"))
                .verifyComplete();
    }
}