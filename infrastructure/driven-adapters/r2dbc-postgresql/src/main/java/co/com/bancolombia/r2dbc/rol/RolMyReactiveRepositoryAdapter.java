package co.com.bancolombia.r2dbc.rol;

import co.com.bancolombia.model.rol.Rol;
import co.com.bancolombia.model.rol.RolEntity;
import co.com.bancolombia.model.rol.gateways.RolRepository;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RolMyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Rol/* change for domain model */,
        RolEntity/* change for adapter model */,
        Long,
        RolMyReactiveRepository
        > implements RolRepository {
    public RolMyReactiveRepositoryAdapter(RolMyReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Rol.class/* change for domain model */));
    }

}
