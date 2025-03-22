package es.dlj.onlinestore.dto;

import java.util.List;

public record ProductTagDTO(
    Long id,
    String name,
    List<ProductSimpleDTO> products
) {}
