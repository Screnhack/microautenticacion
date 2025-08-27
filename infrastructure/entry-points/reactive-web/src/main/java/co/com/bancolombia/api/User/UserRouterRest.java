package co.com.bancolombia.api.User;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(UserHandler handler) {
        return route(GET("/api/v1/usuarios/listar"), handler::listenGETUseCase)
                .andRoute(POST("/api/v1/usuarios"), handler::listenPOSTSaveUserUsesCase)
                .and(route(GET("/api/otherusercase/path"), handler::listenGETOtherUseCase));
    }
}
