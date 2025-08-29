package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.LoginRequest;
import co.com.bancolombia.usecase.auth.AuthUseCase;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthorizationHandler {
    private final AuthUseCase authUseCase;
    private final Validator validator;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(dto -> {
                    var violations = validator.validate(dto);
                    if (!violations.isEmpty())
                        return Mono.error(new ConstraintViolationException(violations));
                    return authUseCase.login(dto.email(), dto.password())
                            .flatMap(data ->
                                    ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(data));
                });
    }
}
