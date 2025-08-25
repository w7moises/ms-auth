package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.model.role.Role;
import co.com.bancolombia.model.role.gateways.RoleRepository;
import co.com.bancolombia.r2dbc.entity.RoleEntity;
import co.com.bancolombia.r2dbc.exception.NotFoundException;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        Long,
        RoleReactiveRepository
        > implements RoleRepository {
    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Role> findRoleBydId(Long id) {
        return super.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("role.notFound.id", id)));
    }

    @Transactional(transactionManager = "r2dbcTransactionManager")
    @Override
    public Mono<Role> saveRole(Role role) {
        return super.save(role)
                .doOnError(ex -> log.error("error({})", ex.getMessage(), ex));
    }
}
