package es.dlj.onlinestore.dto;
import org.mapstruct.Mapper;

import es.dlj.onlinestore.domain.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewDTO toDTO(Review product);
    Review toDomain(ReviewDTO productDTO);
    
}