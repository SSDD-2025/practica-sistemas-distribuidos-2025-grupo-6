package es.dlj.onlinestore.dto;
import org.mapstruct.Mapper;

import es.dlj.onlinestore.domain.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDTO(Product product);
    Product toDomain(ProductDTO productDTO);
    
}
