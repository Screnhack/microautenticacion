package co.com.bancolombia.api.user;

import co.com.bancolombia.api.User.UserHandler;
import co.com.bancolombia.api.User.UserRouterRest;
import co.com.bancolombia.api.user.testdatabuilder.UserUseCaseTestDataBuilder;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.usecase.exception.ExcepcionArgumentos;
import co.com.bancolombia.usecase.exception.ExcepcionCorreoExistente;
import co.com.bancolombia.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class})
public class UserRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserUseCase userUseCase;

    private UserUseCaseTestDataBuilder userBuilder;

    @BeforeEach
    void setUp() {
        userBuilder = new UserUseCaseTestDataBuilder();
    }

    // --- Prueba para el endpoint POST /api/v1/usuarios ---
    @Test
    @DisplayName("Debería guardar un usuario y devolver 200 OK")
    void shouldSaveUser_andReturn_Ok() {
        // Arrange
        User user = userBuilder.withCorreoElectronico("test@example.com").build();
        when(userUseCase.saveUser(any(User.class))).thenReturn(Mono.just(user));

        // Act & Assert
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(User.class)
                .consumeWith(response -> {
                    User returnedUser = response.getResponseBody();
                    assertThat(returnedUser.getCorreoElectronico()).isEqualTo(user.getCorreoElectronico());
                    assertThat(returnedUser.getId()).isEqualTo(user.getId());
                });
    }

    // --- Prueba para la excepción de argumentos inválidos ---
    @Test
    @DisplayName("Debería devolver 400 BAD_REQUEST para argumentos inválidos en POST")
    void shouldReturnBadRequest_forInvalidArguments() {
        // Arrange
        when(userUseCase.saveUser(any(User.class)))
                .thenReturn(Mono.error(new ExcepcionArgumentos("El nombre no puede ser nulo")));

        // Act & Assert
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userBuilder.withNombre("").build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("El nombre no puede ser nulo");
    }

    // --- Prueba para la excepción de correo existente ---
    @Test
    @DisplayName("Debería devolver 400 BAD_REQUEST si el correo ya existe")
    void shouldReturnBadRequest_forExistingEmail() {
        // Arrange
        when(userUseCase.saveUser(any(User.class)))
                .thenReturn(Mono.error(new ExcepcionCorreoExistente("El correo ya existe")));

        // Act & Assert
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userBuilder.build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("El correo ya existe");
    }
}