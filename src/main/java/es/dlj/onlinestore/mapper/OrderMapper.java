
package es.dlj.onlinestore.mapper;
import org.mapstruct.Mapper;

import es.dlj.onlinestore.domain.Order;
import es.dlj.onlinestore.dto.OrderDTO;
import es.dlj.onlinestore.dto.OrderSimpleDTO;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toDTO(Order order);
    OrderSimpleDTO toSimpleDTO(Order order);
    
}