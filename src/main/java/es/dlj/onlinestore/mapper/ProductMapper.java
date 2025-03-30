package es.dlj.onlinestore.mapper;
import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ProductSimpleDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductDTO toDTO(Product product);
    Product toDomain(ProductDTO productDTO);
    Collection <ProductSimpleDTO> toDTOs(Collection<Product> products);
    
    
}
