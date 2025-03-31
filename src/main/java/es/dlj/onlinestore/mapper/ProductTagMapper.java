package es.dlj.onlinestore.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import es.dlj.onlinestore.domain.ProductTag;
import es.dlj.onlinestore.dto.ProductTagDTO;

@Mapper(componentModel = "spring")
public interface ProductTagMapper {

    ProductTagDTO toDTO(ProductTag productTag);
    ProductTag toDomain(ProductTagDTO productTagDTO);
    List<ProductTagDTO> toDTOs(List<ProductTag> productTags);
    List<ProductTag> toDomains(List<ProductTagDTO> productTagDTOs);
}
