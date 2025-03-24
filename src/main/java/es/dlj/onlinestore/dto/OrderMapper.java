
package es.dlj.onlinestore.dto;
import org.mapstruct.Mapper;
import es.dlj.onlinestore.domain.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toDTO(Order order);
    Order toDomain(OrderDTO orderDTO);
    
}