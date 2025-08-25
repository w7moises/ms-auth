package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.UserDto;
import co.com.bancolombia.api.mapper.UserDtoMapperImpl;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.user.UserUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.api.utils.MockData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, UserHandler.class})
@WebFluxTest(excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration.class
})
@Import(UserDtoMapperImpl.class)
class UserRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;

    @Test
    void shouldGetAllUsers() {
        when(userUseCase.findAllUsers()).thenReturn(Flux.just(USER_MOCK, USER_MOCK2));
        webTestClient.get()
                .uri(USERS_BASE)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDto.class)
                .hasSize(2)
                .value(list -> {
                    Assertions.assertThat(list).isNotEmpty();
                    Assertions.assertThat(list.get(0).id()).isEqualTo(1L);
                });
    }

    @Test
    void shouldGetUserById() {
        Long id = 1L;
        when(userUseCase.findUserById(id)).thenReturn(Mono.just(USER_MOCK));
        webTestClient.get()
                .uri(USERS_BASE + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(u -> Assertions.assertThat(u.id()).isEqualTo(id));
    }

    @Test
    void shouldGetUserByEmail() {
        String email = "w7@gmail.com";
        when(userUseCase.findUserByEmail(email)).thenReturn(Mono.just(USER_MOCK));
        webTestClient.get()
                .uri(USERS_BASE + "/email/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(u -> Assertions.assertThat(u.email()).isEqualTo(email));
    }

    @Test
    void shouldGetUserByDocumentNumber() {
        String document = "73727173";
        when(userUseCase.findUserByDocumentNumber(document)).thenReturn(Mono.just(USER_MOCK));
        webTestClient.get()
                .uri(USERS_BASE + "/document/{document}", document)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(u -> Assertions.assertThat(u.documentNumber()).isEqualTo(document));
    }

    @Test
    void shouldPostCreateUser() {
        when(userUseCase.saveUser(any(User.class))).thenReturn(Mono.just(USER_MOCK2));
        webTestClient.post()
                .uri(USERS_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(CREATE_USER_DTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(saved -> {
                    Assertions.assertThat(saved.getId()).isEqualTo(2L);
                    Assertions.assertThat(saved.getEmail()).isEqualTo("wmolina@gmail.com");
                });
    }

    @Test
    void shouldPutUpdateUser() {
        Long id = 1L;
        when(userUseCase.updateUser(any(User.class))).thenReturn(Mono.just(USER_MOCK_UPDATED));
        webTestClient.put()
                .uri(USERS_BASE + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(EDIT_USER_DTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .value(u -> Assertions.assertThat(u.getLastName()).isEqualTo("UPDATE"));
    }

    @Test
    void shouldDeleteUserByEmail() {
        String email = "w7@gmail.com";
        when(userUseCase.deleteByEmail(eq(email))).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri(USERS_BASE + "/email/{email}", email)
                .exchange()
                .expectStatus().isNoContent();
    }
}
