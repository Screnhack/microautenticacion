package co.com.bancolombia.model.user.gateways;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    /**
     *
     * @param user
     * @return
     */
    Mono<User> save(User user);

    Flux<User> findAll();
}
