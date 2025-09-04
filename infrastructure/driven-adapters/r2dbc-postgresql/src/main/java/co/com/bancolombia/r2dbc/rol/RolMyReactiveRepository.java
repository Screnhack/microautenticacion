package co.com.bancolombia.r2dbc.rol;

import co.com.bancolombia.model.rol.Rol;
import co.com.bancolombia.model.rol.RolEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// TODO: This file is just an example, you should delete or modify it
public interface RolMyReactiveRepository extends ReactiveCrudRepository<RolEntity, Long>, ReactiveQueryByExampleExecutor<RolEntity> {

}
