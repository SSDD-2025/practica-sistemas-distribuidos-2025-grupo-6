package es.dlj.onlinestore.dto;

import es.dlj.onlinestore.enumeration.ProductType;

public record ProductSimpleDTO(
    Long id,
    String name,
    float price,
    int stock,
    float sale,
    int totalSells,
    ProductType productType
) {
    public float getProductSale() {
        return ((float) Math.round(price * sale)) / 100f;
    }

    public double getPriceWithSale() {
        return price - getProductSale();
    }

    public boolean getInStock(){
        return stock > 0;
    }
}
