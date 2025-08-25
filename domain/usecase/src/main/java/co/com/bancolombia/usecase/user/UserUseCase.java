package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.role.gateways.RoleRepository;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public Mono<User> saveUser(User user) {
        return roleRepository.findRoleBydId(user.getRoleId()).then(userRepository.saveUser(user));
    }

    public Mono<User> updateUser(User user) {
        return userRepository.updateUser(user);
    }

    public Mono<User> findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    public Mono<User> findUserByDocumentNumber(String documentNumber) {
        return userRepository.findUserByDocumentNumber(documentNumber);
    }

    public Mono<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Flux<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public Mono<Void> deleteByEmail(String email) {
        return userRepository.deleteByEmail(email);
    }
}
