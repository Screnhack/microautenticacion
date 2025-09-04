package co.com.bancolombia.api.User;

import co.com.bancolombia.api.dto.JwtResponse;
import co.com.bancolombia.api.mapper.LoginMapper;
import co.com.bancolombia.api.mapper.UserMapper;
import co.com.bancolombia.model.autenticacion.Autenticacion;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.autenticacion.AutenticacionUseCase;
import co.com.bancolombia.usecase.exception.ExcepcionArgumentos;
import co.com.bancolombia.usecase.exception.ExcepcionCorreoExistente;
import co.com.bancolombia.usecase.user.UserUseCase;
import co.com.bancolombia.usecase.user.UserUseCaseValidateEmail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Tag(name = "Usuario", description = "Gestión de usuarios.")
public class UserHandler {
    private static final Logger log = LoggerFactory.getLogger(UserHandler.class);

    private final UserUseCase userUseCase;
    private final UserUseCaseValidateEmail userValidateEmail;
    private final UserMapper userMapper;
    private final AutenticacionUseCase autenticacionUseCase;
    private final LoginMapper loginMapper;

    @Operation(
            summary = "Guarda un nuevo usuario",
            description = "Crea un nuevo usuario en el sistema. Retorna el usuario creado con su ID.",
            requestBody = @RequestBody(
                    description = "Datos del usuario a crear.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = co.com.bancolombia.api.dto.UserCreateRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario guardado exitosamente.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error de validación o usuario existente.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))
                    )
            }
    )
    public Mono<ServerResponse> listenPOSTSaveUserUsesCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(co.com.bancolombia.api.dto.UserCreateRequest.class)
                .flatMap(userRequest -> {
                    User user = userMapper.toUser(userRequest);
                    return userUseCase.saveUser(user);
                })
                .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user))
                .onErrorResume(ExcepcionArgumentos.class, this::handleError)
                .onErrorResume(ExcepcionCorreoExistente.class, this::handleError);
    }

    @Operation(summary = "Lista todos los usuarios", description = "Retorna una lista completa de todos los usuarios registrados.")
    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("");
    }

    @Operation(summary = "Endpoint de ejemplo", description = "Describe un caso de uso alternativo.")
    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("");
    }

    @Operation(
            summary = "Valida si un correo ya existe",
            description = "Verifica si el correo electrónico proporcionado ya está registrado en la base de datos.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.QUERY,
                            name = "correo",
                            description = "El correo electrónico a validar.",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Respuesta de validación exitosa.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Boolean.class, example = "false")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "El formato del correo electrónico es inválido.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Error.class)
                            )
                    )
            }
    )
    public Mono<ServerResponse> listenGETValidateUserEmail(ServerRequest serverRequest) {
        String correo = serverRequest.queryParam("correo").orElse(null);
        return userValidateEmail.validateUserEmail(correo)
                .flatMap(emailExists ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(emailExists)
                )
                .onErrorResume(ExcepcionArgumentos.class, this::handleError);
    }

    @Operation(summary = "Inicia sesión y genera un JWT", description = "Autentica al usuario y retorna un token JWT para peticiones seguras.")
    public Mono<ServerResponse> listenPOSTLoginUser(ServerRequest request) {
        return request.bodyToMono(Autenticacion.class)
                .flatMap(loginRequest ->
                        autenticacionUseCase.login(loginRequest.getCorreo(), loginRequest.getPassword())
                                .flatMap(token -> ServerResponse.ok().bodyValue(new JwtResponse(token)))
                                .onErrorResume(BadCredentialsException.class, e -> ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
                );
    }

    private Mono<ServerResponse> handleError(RuntimeException e) {
        Error error = Error.builder()
                .codigo(e instanceof ExcepcionArgumentos ? ((ExcepcionArgumentos) e).getCodigo() : 500)
                .mensaje(e.getMessage())
                .build();

        return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(error);
    }
}
