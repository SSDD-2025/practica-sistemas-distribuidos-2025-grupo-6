package es.dlj.onlinestore.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.dlj.onlinestore.domain.User;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageMapper imageMapper;

    public abstract UserDTO toDTO(User user);
    public abstract UserSimpleDTO toSimpleDTO(User user);
    public abstract UserSimpleDTO toSimpleDTO(UserDTO userDTO);

    @Mapping(target = "profilePhoto", expression = "java(imageMapper.toDomain(userFormDTO.getProfilePhoto()))")
    public abstract User toDomain(UserSimpleDTO userSimpleDTO);

    @Mapping(target = "encodedPassword", expression = "java(passwordEncoder.encode(UserFormDTO.getPassword()))")
    @Mapping(target = "profilePhoto", expression = "java(imageMapper.toDomain(userFormDTO.getProfilePhoto()))")
    @Mapping(target = "roles", expression = "java(List.of(\"USER\"))")
    public abstract User toDomain(UserFormDTO userFormDTO);
    
    @Mapping(target = "profilePhoto", expression = "java(imageMapper.toDomain(userFormDTO.getProfilePhoto()))")
    public abstract User toDomain(UserDTO userDTO);
}