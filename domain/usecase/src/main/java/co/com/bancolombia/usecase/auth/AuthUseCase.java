package co.com.bancolombia.usecase.auth;

import co.com.bancolombia.model.login.LoginResponse;
import co.com.bancolombia.model.login.gateways.PasswordHasher;
import co.com.bancolombia.model.login.gateways.TokenGenerator;
import co.com.bancolombia.model.role.Role;
import co.com.bancolombia.model.role.gateways.RoleRepository;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class AuthUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;

    public Mono<LoginResponse> login(String email, String password) {
        return userRepository.findUserByEmail(email)
                .flatMap(user -> passwordHasher.verifyPassword(user, password))
                .flatMap(user ->
                        roleRepository.findRoleBydId(user.getRoleId())
                                .map(Role::getName)
                                .defaultIfEmpty("USER")
                                .map(List::of)
                                .map(roles -> new LoginResponse(tokenGenerator.generate(email, roles, user.getId())))
                );
    }
}
