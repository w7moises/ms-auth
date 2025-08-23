package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(UserHandler userHandler) {
        return RouterFunctions
                .route()
                .path("/api/v1/users", builder -> builder
                        .POST("", userHandler::createUser)
                        .GET("", userHandler::getAllUsers)
                        .GET("/{id}", userHandler::getUserById)
                        .PUT("/{id}", userHandler::updateUser)
                        .GET("/by-email/{email}", userHandler::getUserByEmail)
                        .DELETE("/{email}", userHandler::deleteUserByEmail)
                )
                .build();
    }
}
