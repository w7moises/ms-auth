package co.com.bancolombia.usecase.role;

import co.com.bancolombia.model.role.Role;
import co.com.bancolombia.model.role.gateways.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleUseCaseTest {

    @Mock
    RoleRepository roleRepository;

    RoleUseCase useCase;

    private static final Role ROLE_ADMIN = Role.builder()
            .id(1L).name("ADMIN").description("Administrador").build();

    private static final Role ROLE_TO_SAVE = Role.builder()
            .name("AUDITOR").description("Auditor del sistema").build();

    private static final Role ROLE_SAVED = ROLE_TO_SAVE.toBuilder()
            .id(5L).build();

    @BeforeEach
    void setUp() {
        useCase = new RoleUseCase(roleRepository);
    }

    @Test
    void shouldFindRoleById() {
        when(roleRepository.findRoleBydId(1L)).thenReturn(Mono.just(ROLE_ADMIN));

        StepVerifier.create(useCase.findRoleBydId(1L))
                .expectNext(ROLE_ADMIN)
                .verifyComplete();
    }

    @Test
    void shouldSaveRole() {
        when(roleRepository.saveRole(ROLE_TO_SAVE)).thenReturn(Mono.just(ROLE_SAVED));
        StepVerifier.create(useCase.saveRole(ROLE_TO_SAVE))
                .expectNext(ROLE_SAVED)
                .verifyComplete();
    }
}
