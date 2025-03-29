package es.dlj.onlinestore.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.dto.UserDTO;
import es.dlj.onlinestore.dto.UserFormDTO;
import es.dlj.onlinestore.dto.UserSimpleDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected ImageMapper imageMapper;

    public abstract UserDTO toDTO(User user);
    public abstract UserSimpleDTO toSimpleDTO(User user);
    public abstract UserSimpleDTO toSimpleDTO(UserDTO userDTO);

    @Mapping(target = "profilePhoto", expression = "java(this.imageMapper.toDomain(userSimpleDTO.profilePhoto()))")
    public abstract User toDomain(UserSimpleDTO userSimpleDTO);

    @Mapping(target = "encodedPassword", expression = "java(this.passwordEncoder.encode(userFormDTO.password()))")
    @Mapping(target = "roles", expression = "java(List.of(\"USER\"))")
    public abstract User toDomain(UserFormDTO userFormDTO);

    public abstract UserDTO toDTO(UserFormDTO userFormDTO);
    
    @Mapping(target = "profilePhoto", expression = "java(this.imageMapper.toDomain(userDTO.profilePhoto()))")
    public abstract User toDomain(UserDTO userDTO);
    public abstract Collection<UserDTO> toDTOs(List<User> all);
    
}