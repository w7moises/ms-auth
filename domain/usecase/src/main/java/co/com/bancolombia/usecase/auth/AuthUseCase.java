package co.com.bancolombia.usecase.auth;

import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthUseCase {
    private final UserRepository userRepository;

}
