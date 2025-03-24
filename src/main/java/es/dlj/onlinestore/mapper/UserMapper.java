package es.dlj.onlinestore.mapper;

import java.util.Collection;

import org.mapstruct.Mapper;

import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.dto.UserDTO;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User product);
    User toDomain(UserDTO productDTO);
    Collection<UserDTO> toDTOs(Collection<User> products);
    Collection<User> toDomains(Collection<UserDTO> productDTOs);
    
}