package co.com.bancolombia.model.autenticacion.gateways;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Mono;

public interface AutenticacionRepository {
    Mono<String> generateToken(User user);
}
