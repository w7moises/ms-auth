package co.com.bancolombia.model.user.gateways;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<String> saveUser(User user);

    Mono<User> findUserById(Long id);

    Mono<User> findUserByEmail(String email);

    Flux<User> findAllUsers();

    Mono<String> deleteByEmail(String email);
}
