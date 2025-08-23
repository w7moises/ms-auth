package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateUserDto;
import co.com.bancolombia.api.dto.EditUserDto;
import co.com.bancolombia.api.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "createUser",
                    operation = @Operation(
                            operationId = "createUser",
                            summary = "Crear usuario",
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = CreateUserDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Validacion Fallida",
                                            content = @Content(schema = @Schema(type = "string", example = "Usuario guardado con id:"))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Validacion Fallida",
                                            content = @Content(schema = @Schema(ref = "#/components/schemas/ApiError"))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/{id}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.PUT,
                    beanClass = UserHandler.class,
                    beanMethod = "updateUser",
                    operation = @Operation(
                            operationId = "updateUser",
                            summary = "Actualizar usuario por id",
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, required = true)
                            },
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = EditUserDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Actualizado"
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "No encontrado",
                                            content = @Content(schema = @Schema(ref = "#/components/schemas/ApiError"))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Validacion Fallida",
                                            content = @Content(schema = @Schema(ref = "#/components/schemas/ApiError"))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/{id}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "getUserById",
                    operation = @Operation(
                            operationId = "getUserById",
                            summary = "Obtener usuario por id",
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, required = true)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "OK",
                                            content = @Content(schema = @Schema(implementation = UserDto.class))),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "No encontrado",
                                            content = @Content(schema = @Schema(ref = "#/components/schemas/ApiError"))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/email/{email}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "getUserByEmail",
                    operation = @Operation(
                            operationId = "getUserByEmail",
                            summary = "Obtener usuario por email",
                            parameters = {
                                    @Parameter(name = "email", in = ParameterIn.PATH, required = true)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = UserDto.class)
                                            )

                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "No encontrado",
                                            content = @Content(schema = @Schema(ref = "#/components/schemas/ApiError"))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "getAllUsers",
                    operation = @Operation(
                            operationId = "getAllUsers",
                            summary = "Listar usuarios",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de usuarios",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users/email/{email}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.DELETE,
                    beanClass = UserHandler.class,
                    beanMethod = "deleteUserByEmail",
                    operation = @Operation(
                            operationId = "deleteUserByEmail",
                            summary = "Eliminar usuario por email",
                            parameters = {
                                    @Parameter(name = "email", in = ParameterIn.PATH, required = true)
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200"
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "No encontrado",
                                            content = @Content(schema = @Schema(ref = "#/components/schemas/ApiError"))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UserHandler userHandler) {
        return RouterFunctions
                .route()
                .path("/api/v1/users", builder -> builder
                        .POST("", userHandler::createUser)
                        .GET("", userHandler::getAllUsers)
                        .GET("/{id}", userHandler::getUserById)
                        .PUT("/{id}", userHandler::updateUser)
                        .GET("/email/{email}", userHandler::getUserByEmail)
                        .DELETE("/email/{email}", userHandler::deleteUserByEmail)
                )
                .build();
    }
}
