package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.UserCreateRequest;
import co.com.bancolombia.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idRol", ignore = true)
    User toUser(UserCreateRequest userRequest);
}
