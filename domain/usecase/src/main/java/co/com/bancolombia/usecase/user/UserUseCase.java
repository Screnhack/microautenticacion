package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.exception.ExcepcionArgumentos;
import co.com.bancolombia.usecase.exception.ExcepcionCorreoExistente;
import co.com.bancolombia.usecase.validator.argumentValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private static final String NOMBRE_NO_PUEDE_SER_NULO_O_VACIO = "El nombre no puede ser nulo o vacío";
    private static final String EL_APELLIDO_NO_PUEDE_SER_NULO_O_VACIO = "El apellido no puede ser nulo o vacío";
    private static final String EL_CORREO_ELECTRONICO_NO_PUEDE_SER_NULO_O_VACIO = "El correo electrónico no puede ser nulo o vacío";
    private static final String EL_SALARIO_BASE_DEBE_SER_MAYOR_A_CERO = "El salario base debe ser mayor a cero";
    private static final String EL_CORREO_ELECTRONICO_INGRESADO_YA_SE_ENCUENTRA_REGISTRADO = "El correo electronico ingresado ya se encuentra registrado";
    private static final String EL_FORMATO_DEL_CORREO_ES_ERRONEO = "El formato del correo es erroneo";
    private static final Long MIN = 0L;
    private static final Long MAX = 15000000L;
    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        try {
            argumentValidator.validarRequeridos(user.getNombre(), NOMBRE_NO_PUEDE_SER_NULO_O_VACIO);
            argumentValidator.validarRequeridos(user.getApellido(), EL_APELLIDO_NO_PUEDE_SER_NULO_O_VACIO);
            argumentValidator.validarRequeridos(user.getCorreoElectronico(), EL_CORREO_ELECTRONICO_NO_PUEDE_SER_NULO_O_VACIO);
            argumentValidator.validarRango(user.getSalarioBase(), MIN, MAX, EL_SALARIO_BASE_DEBE_SER_MAYOR_A_CERO);
            argumentValidator.validarFormatoEmail(user.getCorreoElectronico(), EL_FORMATO_DEL_CORREO_ES_ERRONEO);
        } catch (ExcepcionArgumentos e) {
            return Mono.error(e);
        }
        return validacionCorreoElectronico(user)
                .flatMap(correoExistente -> {
                    if (Boolean.TRUE.equals(correoExistente)) {
                        return Mono.error(new ExcepcionCorreoExistente(EL_CORREO_ELECTRONICO_INGRESADO_YA_SE_ENCUENTRA_REGISTRADO));
                    } else {
                        return userRepository.save(user);
                    }
                });

    }

    private Mono<Boolean> validacionCorreoElectronico(User user) {
        return userRepository.findAll()
                .map(User::getCorreoElectronico)
                .any(correo -> correo.equals(user.getCorreoElectronico()));
    }
}
