package co.com.bancolombia.model.rol.gateways;

import co.com.bancolombia.model.rol.Rol;
import reactor.core.publisher.Flux;

public interface RolRepository {
    /**
     * @return
     */
    Flux<Rol> findAll();
}
