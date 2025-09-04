package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.UserLoginRequest;
import co.com.bancolombia.model.autenticacion.Autenticacion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LoginMapper {
    LoginMapper INSTANCE = Mappers.getMapper(LoginMapper.class);

    Autenticacion toLoginAutentication(UserLoginRequest userRequest);
}
