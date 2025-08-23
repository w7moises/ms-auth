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
import org.springframework.transaction.annotation.Transactional;
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
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Transactional(transactionManager = "r2dbcTransactionManager")
    @Override
    public Mono<User> saveUser(User user) {
        return repository.existsByEmail(user.getEmail())
                .flatMap(exists -> exists
                        ? Mono.error(new FoundException("user.email.alreadyExists", user.getEmail()))
                        : super.save(user)
                )
                .doOnError(ex -> log.error("error({})", ex.getMessage(), ex));
    }

    @Transactional(transactionManager = "r2dbcTransactionManager")
    @Override
    public Mono<User> updateUser(User user) {
        return super.findById(user.getId())
                .switchIfEmpty(Mono.error(new NotFoundException("user.notFound.id", user.getId())))
                .flatMap(actual -> {
                    String newEmail = user.getEmail();
                    String oldEmail = actual.getEmail();
                    if (newEmail.equalsIgnoreCase(oldEmail))
                        return super.save(user);
                    return repository.findByEmail(newEmail)
                            .flatMap(found -> {
                                if (found.getId().equals(user.getId()))
                                    return super.save(user);
                                return Mono.error(new FoundException("user.email.alreadyExists", newEmail));
                            })
                            .switchIfEmpty(super.save(user));
                })
                .doOnError(ex -> log.error("error({})", ex.getMessage(), ex));
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<User> findUserById(Long id) {
        return super.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("user.notFound.id", id)));
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<User> findUserByEmail(String email) {
        return repository.findByEmail(email)
                .map(this::toEntity)
                .switchIfEmpty(Mono.error(new NotFoundException("user.notFound.email", email)));
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<User> findAllUsers() {
        return super.findAll()
                .doOnError(ex -> log.error("error({})", ex.getMessage(), ex));
    }

    @Transactional(transactionManager = "r2dbcTransactionManager")
    @Override
    public Mono<Void> deleteByEmail(String email) {
        return repository.findByEmail(email)
                .switchIfEmpty(Mono.error(new NotFoundException("user.notFound.email", email)))
                .flatMap(entity -> repository.deleteById(entity.getId()))
                .doOnError(ex -> log.error("error ({}): {}", email, ex.getMessage(), ex));
    }
}
