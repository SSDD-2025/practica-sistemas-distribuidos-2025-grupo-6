
package es.dlj.onlinestore.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import es.dlj.onlinestore.domain.Order;
import es.dlj.onlinestore.dto.OrderDTO;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toDTO(Order order);
    Order toDomain(OrderDTO orderDTO);
    
}