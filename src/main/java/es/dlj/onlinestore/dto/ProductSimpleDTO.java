package es.dlj.onlinestore.dto;

public record ProductSimpleDTO(
    Long id,
    String name,
    float price,
    int stock,
    float sale,
    int totalSells
) {}
