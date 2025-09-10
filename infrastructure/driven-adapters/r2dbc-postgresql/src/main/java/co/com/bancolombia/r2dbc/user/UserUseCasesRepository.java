package co.com.bancolombia.r2dbc.user;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.r2dbc.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserUseCasesRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<Boolean> existsByCorreoElectronico(String email);

    @Query("SELECT u.id_usuario, u.nombre, u.apellido, u.fecha_nacimiento, u.direccion, u.telefono, u.correo_electronico, u.salario_base, u.password, r.id_rol, r.nombre as nombre_rol " +
            "FROM usuario u " +
            "INNER JOIN rol r ON u.id_rol = r.id_rol " +
            "WHERE u.correo_electronico = :correoElectronico")
    Mono<User> findByCorreoElectronico(String correoElectronico);
}
