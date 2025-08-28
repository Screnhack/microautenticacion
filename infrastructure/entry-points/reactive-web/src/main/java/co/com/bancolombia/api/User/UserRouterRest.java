package co.com.bancolombia.api.User;

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
public class UserRouterRest {

    @Bean
    @RouterOperations({
            // Documentación completa para el endpoint POST
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "listenPOSTSaveUserUsesCase"
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/listar",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "listenGETUseCase"
            ),
            // Documentación para el endpoint GET /otherusercase
            @RouterOperation(
                    path = "/api/otherusercase/path",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "listenGETOtherUseCase"
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UserHandler handler) {
        return route(POST("/api/v1/usuarios").and(accept(MediaType.APPLICATION_JSON)), handler::listenPOSTSaveUserUsesCase)
                .andRoute(GET("/api/v1/usuarios/listar"), handler::listenGETUseCase)
                .and(route(GET("/api/otherusercase/path"), handler::listenGETOtherUseCase));
    }
}
