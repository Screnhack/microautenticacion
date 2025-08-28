package co.com.bancolombia.usecase.user;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.usecase.exception.ExcepcionArgumentos;
import co.com.bancolombia.usecase.exception.ExcepcionCorreoExistente;
import co.com.bancolombia.usecase.user.testdatabuilder.UserUseCaseTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    private UserUseCaseTestDataBuilder userBuilder;

    @BeforeEach
    void setUp() {
        userBuilder = new UserUseCaseTestDataBuilder();
    }

    @Test
    @DisplayName("Debería guardar un usuario exitosamente cuando las validaciones son correctas")
    void saveUser_success() {
        // Arrange
        User user = userBuilder.build();
        when(userRepository.findAll()).thenReturn(Flux.empty());
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));

        // Act
        Mono<User> result = userUseCase.saveUser(user);

        // Assert
        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionArgumentos si el nombre es nulo")
    void saveUser_nombreNulo() {
        // Arrange
        User user = userBuilder.withNombre(null).build();

        // Act
        Mono<User> result = userUseCase.saveUser(user);

        // Assert
        StepVerifier.create(result).expectErrorMatches(e -> e
                instanceof ExcepcionArgumentos && e.getMessage().equals("El nombre no puede ser nulo o vacío")).verify();
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionArgumentos si el correo es inválido")
    void saveUser_correoInvalido() {
        // Arrange
        User user = userBuilder.withCorreoElectronico("correo.invalido").build();

        // Act
        Mono<User> result = userUseCase.saveUser(user);

        // Assert
        StepVerifier.create(result).expectErrorMatches(e -> e
                instanceof ExcepcionArgumentos && e.getMessage().equals("El formato del correo es erroneo")).verify();
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionCorreoExistente si el correo ya está registrado")
    void saveUser_correoExistente() {
        // Arrange
        User userExistente = userBuilder.build();
        User nuevoUsuario = userBuilder.withId(2L).build();
        when(userRepository.findAll()).thenReturn(Flux.just(userExistente));

        // Act
        Mono<User> result = userUseCase.saveUser(nuevoUsuario);

        // Assert
        StepVerifier.create(result).expectErrorMatches(e -> e
                instanceof ExcepcionCorreoExistente &&
                e.getMessage().equals("El correo electronico ingresado ya se encuentra registrado")).verify();
    }
}