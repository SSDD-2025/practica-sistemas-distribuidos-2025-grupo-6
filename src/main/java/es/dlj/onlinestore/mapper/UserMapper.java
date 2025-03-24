package es.dlj.onlinestore.mapper;

import org.mapstruct.Mapper;

import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.dto.UserDTO;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User product);
    User toDomain(UserDTO productDTO);
    
}