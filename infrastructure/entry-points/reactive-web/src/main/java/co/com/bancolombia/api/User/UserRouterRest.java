package co.com.bancolombia.api.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Tag(name = "Usuarios", description = "Endpoints para la gestión de usuarios")
public class UserRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = org.springframework.web.bind.annotation.RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "listenPOSTSaveUserUsesCase",
                    operation = @Operation(summary = "Guarda un usuario")
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/listar",
                    method = org.springframework.web.bind.annotation.RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "listenGETUseCase",
                    operation = @Operation(summary = "Lista usuarios")
            ),
            @RouterOperation(
                    path = "/api/v1/usuario/correo",
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "listenGETValidateUserEmail",
                    operation = @Operation(summary = "verifica el correo existente")
            ),
            // Ruta de login (debe ser pública)
            @RouterOperation(
                    path = "/api/v1/login",
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "listenPOSTLoginUser",
                    operation = @Operation(summary = "Inicia sesión y genera un JWT")
            )

    })
    public RouterFunction<ServerResponse> routerFunction(UserHandler handler) {
        return route(POST("/api/v1/usuarios").and(accept(MediaType.APPLICATION_JSON)), handler::listenPOSTSaveUserUsesCase)
                .andRoute(POST("/api/v1/login").and(accept(MediaType.APPLICATION_JSON)), handler::listenPOSTLoginUser)
                .andRoute(GET("/api/v1/usuario/correo"), handler::listenGETValidateUserEmail)
                .andRoute(GET("/api/v1/usuarios/listar"), handler::listenGETUseCase)
                .and(route(GET("/api/otherusercase/path"), handler::listenGETOtherUseCase));
    }
}
