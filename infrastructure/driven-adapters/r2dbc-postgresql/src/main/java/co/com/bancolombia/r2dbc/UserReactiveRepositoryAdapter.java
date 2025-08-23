package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import co.com.bancolombia.r2dbc.exception.FoundException;
import co.com.bancolombia.r2dbc.exception.NotFoundException;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        UserReactiveRepository
        > implements UserRepository {
    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<String> saveUser(User user) {
        return repository.existsByEmail(user.getEmail())
                .flatMap(exists -> exists
                        ? Mono.error(new FoundException("El email ya está registrado: " + user.getEmail()))
                        : super.save(user).map(saved -> "Usuario guardado con id: " + saved.getId())
                )
                .doOnError(ex -> log.error("error({})", ex.getMessage(), ex));
    }

    @Override
    public Mono<String> updateUser(User user) {
        return super.findById(user.getId())
                .switchIfEmpty(Mono.error(new NotFoundException("Usuario no encontrado con id: " + user.getId())))
                .flatMap(actual -> {
                    String newEmail = user.getEmail();
                    String oldEmail = actual.getEmail();
                    if (newEmail.equalsIgnoreCase(oldEmail))
                        return super.save(user)
                                .map(u -> "Usuario actualizado con id: " + u.getId());
                    return repository.findByEmail(newEmail)
                            .flatMap(found -> {
                                if (found.getId().equals(user.getId()))
                                    return super.save(user)
                                            .map(u -> "Usuario actualizado con id: " + u.getId());
                                return Mono.error(new FoundException("El email ya está registrado: " + newEmail));
                            })
                            .switchIfEmpty(super.save(user).map(u -> "Usuario actualizado con id: " + u.getId()));
                })
                .doOnError(ex -> log.error("error({})", ex.getMessage(), ex));
    }

    @Override
    public Mono<User> findUserById(Long id) {
        return super.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Usuario no encontrado con id: " + id)));
    }

    @Override
    public Mono<User> findUserByEmail(String email) {
        return repository.findByEmail(email)
                .map(this::toEntity)
                .switchIfEmpty(Mono.error(new NotFoundException("Usuario no encontrado con email: " + email)));
    }

    @Override
    public Flux<User> findAllUsers() {
        return super.findAll();
    }

    @Override
    public Mono<String> deleteByEmail(String email) {
        return repository.findByEmail(email)
                .switchIfEmpty(Mono.error(new NotFoundException("Usuario no encontrado con email: " + email)))
                .flatMap(entity ->
                        repository.deleteById(entity.getId())
                                .thenReturn("Usuario eliminado con correo: " + email)
                )
                .doOnError(ex -> log.error("Error eliminando usuario {}: {}", email, ex.getMessage(), ex));
    }
}
