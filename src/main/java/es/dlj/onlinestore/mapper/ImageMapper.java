package es.dlj.onlinestore.mapper;

import org.mapstruct.Mapper;

import es.dlj.onlinestore.domain.Image;
import es.dlj.onlinestore.dto.ImageDTO;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDTO toDTO(Image image);
    Image toDomain(ImageDTO imageDTO);
    
}