package co.com.bancolombia.model.user.gateways;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    /**
     * @param user
     * @return
     */
    Mono<User> save(User user);

    /**
     * @param email
     * @return
     */
    Mono<Boolean> validateUserEmail(String email);

    /**
     * @return
     */
    Flux<User> findAll();

    /**
     * @param correo
     * @return
     */
    Mono<User> findByCorreoElectronico(String correo);
}
