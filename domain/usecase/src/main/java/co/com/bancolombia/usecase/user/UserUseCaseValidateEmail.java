package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.validator.argumentValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCaseValidateEmail {

    private static final String EL_FORMATO_DEL_CORREO_ES_ERRONEO = "El formato del correo es erroneo.";

    private final UserRepository userRepository;

    public Mono<Boolean> validateUserEmail(String correo) {

        return Mono.just(correo)
                .doOnNext(email -> argumentValidator.validarFormatoEmail(email, EL_FORMATO_DEL_CORREO_ES_ERRONEO))
                .flatMap(email -> userRepository.validateUserEmail(email))
                .flatMap(emailExists -> {
                    return emailExists ? Mono.just(true) : Mono.just(false);
                });
    }
}
