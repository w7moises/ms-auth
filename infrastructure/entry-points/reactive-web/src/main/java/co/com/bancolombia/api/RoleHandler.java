package co.com.bancolombia.api;

import co.com.bancolombia.model.role.Role;
import co.com.bancolombia.usecase.role.RoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RoleHandler {

    private final RoleUseCase roleUseCase;

    public Mono<ServerResponse> createRole(ServerRequest request) {
        return request.bodyToMono(Role.class)
                .flatMap(roleUseCase::saveRole)
                .flatMap(state -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(state));
    }

    public Mono<ServerResponse> getRoleById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return roleUseCase.findRoleBydId(id)
                .flatMap(role -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(role));
    }

}
