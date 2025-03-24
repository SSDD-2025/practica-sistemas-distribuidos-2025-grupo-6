package es.dlj.onlinestore.mapper;
import org.mapstruct.Mapper;

import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.dto.ProductDTO;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDTO(Product product);
    Product toDomain(ProductDTO productDTO);
    
}
