package es.dlj.onlinestore.dto;

public record ProductSimpleDTO(
    Long id,
    String name,
    float price,
    int stock,
    float sale,
    int totalSells
) {
    public float getProductSale() {
        return ((float) Math.round(price * sale)) / 100f;
    }

    public float getPriceWithSale() {
        return price - getProductSale();
    }
}
