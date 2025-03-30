package es.dlj.onlinestore.mapper;
import java.util.List;

import org.mapstruct.Mapper;

import es.dlj.onlinestore.domain.Review;
import es.dlj.onlinestore.dto.ReviewDTO;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewDTO toDTO(Review product);
    Review toDomain(ReviewDTO productDTO);
    List<ReviewDTO> toDTOs(List<Review> all);
    
}