package co.com.bancolombia.api.User;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.exception.ExcepcionArgumentos;
import co.com.bancolombia.usecase.exception.ExcepcionCorreoExistente;
import co.com.bancolombia.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private static final Logger log = LoggerFactory.getLogger(UserHandler.class);
    public static final String PROCESANDO_LA_PETICION_PARA_EL_USUARIO_CON_CORREO = "Procesando la petición para el usuario con correo: {}";
    public static final String LA_VALIDACION_DE_LA_SOLICITUD_FALLO = "La validación de la solicitud falló: {}";
    public static final String USUARIO_GUARDADO_EXITOSAMENTE_CON_ID = "Usuario guardado exitosamente con ID: {}";
    public static final String ERROR_DE_NEGOCIO = "Error de negocio: {}";
    public static final String PETICION_RECIBIDA_PARA_GUARDAR_UN_NUEVO_USUARIO = "Petición recibida para guardar un nuevo usuario";
    private final UserUseCase userUseCase;


    public Mono<ServerResponse> listenPOSTSaveUserUsesCase(ServerRequest serverRequest) {


        log.info(PETICION_RECIBIDA_PARA_GUARDAR_UN_NUEVO_USUARIO);

        return serverRequest.bodyToMono(User.class)
                .doOnNext(user -> log.debug(PROCESANDO_LA_PETICION_PARA_EL_USUARIO_CON_CORREO, user.getCorreoElectronico()))
                .flatMap(userUseCase::saveUser)
                .flatMap(user -> {
                    log.info(USUARIO_GUARDADO_EXITOSAMENTE_CON_ID, user.getId());
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(user);
                })
                .onErrorResume(ExcepcionArgumentos.class, e -> {
                    log.error(LA_VALIDACION_DE_LA_SOLICITUD_FALLO, e.getMessage());
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(e.getMessage());
                })
                .onErrorResume(ExcepcionCorreoExistente.class, e -> {
                    log.error(ERROR_DE_NEGOCIO, e.getMessage());
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(e.getMessage());
                });
    }

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("");
    }
}
