package es.dlj.onlinestore.dto;

import org.mapstruct.Mapper;

import es.dlj.onlinestore.domain.User;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User product);
    User toDomain(UserDTO productDTO);
    
}