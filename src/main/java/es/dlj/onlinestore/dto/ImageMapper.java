package es.dlj.onlinestore.dto;

import org.mapstruct.Mapper;

import es.dlj.onlinestore.domain.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDTO toDTO(Image image);
    Image toDomain(ImageDTO imageDTO);
    
}