package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDto;
import co.com.bancolombia.api.dto.EditUserDto;
import co.com.bancolombia.api.dto.UserDto;
import co.com.bancolombia.api.mapper.UserDtoMapper;
import co.com.bancolombia.usecase.user.UserUseCase;
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
public class UserHandler {

    private final UserUseCase userUseCase;
    private final Validator validator;
    private final UserDtoMapper mapper;

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(CreateUserDto.class)
                .flatMap(dto -> {
                    var violations = validator.validate(dto);
                    if (!violations.isEmpty())
                        return Mono.error(new ConstraintViolationException(violations));
                    return userUseCase.saveUser(mapper.toModel(dto)).flatMap(data -> ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(data));
                });
    }

    public Mono<ServerResponse> updateUser(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.bodyToMono(EditUserDto.class)
                .flatMap(dto -> {
                    var violations = validator.validate(dto);
                    if (!violations.isEmpty())
                        return Mono.error(new ConstraintViolationException(violations));
                    var userToUpdate = mapper.toModel(dto);
                    userToUpdate.setId(id);
                    return userUseCase.updateUser(userToUpdate).flatMap(data -> ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(data));
                });
    }

    public Mono<ServerResponse> getUserById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return userUseCase.findUserById(id)
                .map(mapper::toResponse)
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }

    public Mono<ServerResponse> getUserByEmail(ServerRequest request) {
        String email = request.pathVariable("email");
        return userUseCase.findUserByEmail(email)
                .map(mapper::toResponse)
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userUseCase.findAllUsers().map(mapper::toResponse), UserDto.class);
    }

    public Mono<ServerResponse> deleteUserByEmail(ServerRequest request) {
        String email = request.pathVariable("email");
        return userUseCase.deleteByEmail(email)
                .then(ServerResponse.noContent().build());
    }

}
