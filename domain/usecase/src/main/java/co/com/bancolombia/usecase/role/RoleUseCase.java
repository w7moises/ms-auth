package co.com.bancolombia.usecase.role;

import co.com.bancolombia.model.role.Role;
import co.com.bancolombia.model.role.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RoleUseCase {
    private final RoleRepository roleRepository;

    public Mono<Role> findRoleBydId(Long id) {
        return roleRepository.findRoleBydId(id);
    }

    public Mono<Role> saveRole(Role role) {
        return roleRepository.saveRole(role);
    }
}
