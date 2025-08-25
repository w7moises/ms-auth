package co.com.bancolombia.model.role.gateways;

import co.com.bancolombia.model.role.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> findRoleBydId(Long id);

    Mono<Role> saveRole(Role role);
}
