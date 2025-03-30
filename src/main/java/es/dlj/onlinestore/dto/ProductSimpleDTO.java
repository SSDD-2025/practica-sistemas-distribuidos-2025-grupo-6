package es.dlj.onlinestore.dto;

import org.hibernate.validator.constraints.Range;

import es.dlj.onlinestore.enumeration.ProductType;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductSimpleDTO(
    Long id,
    
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    String name,

    @Size(min = 3, max = 2000, message = "Description must be between 3 and 2000 characters")
    String description,

    @Positive(message = "Price must be positive")
    float price,

    @PositiveOrZero(message = "Stock must be positive or zero")
    int stock,

    @Range(min = 0, max = 100, message = "Sale must be between 0 and 100")
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
