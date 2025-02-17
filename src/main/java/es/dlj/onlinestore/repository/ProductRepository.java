package es.dlj.onlinestore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.dlj.onlinestore.enumeration.ProductType;
import es.dlj.onlinestore.model.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByPriceBetween(float minPrice, float maxPrice);
    List<Product> findByProductType(ProductType type);
    List<Product> findByNameContaining(String name);
    Long countByProductType(ProductType type);

    // Best Sellers
    List<Product> findTop10ByOrderByTotalSellsDesc();

    // Top Rated
    List<Product> findTop10ByOrderByRatingDesc();

    // On Sale
    List<Product> findBySaleGreaterThan(int sale);

    // Trending This Week
    List<Product> findTop10ByOrderByLastWeekSellsDesc();

    // Low Stock
    List<Product> findByStockLessThan(int stock);

    


    @Query("SELECT p FROM Product p WHERE "
        + "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
        + "AND (:minPrice IS NULL OR p.price >= :minPrice) "
        + "AND (:maxPrice IS NULL OR p.price <= :maxPrice) "
        + "AND (:tags IS NULL OR NOT EXISTS (SELECT t FROM ProductTag t WHERE t NOT MEMBER OF p.tags AND t.name IN :tags))"
        + "AND (:productTypes IS NULL OR p.productType IN :productTypes) "
        + "AND (:minSale IS NULL OR p.sale >= :minSale) "
        + "AND (:maxSale IS NULL OR p.sale <= :maxSale) "
        + "AND (:minRating IS NULL OR p.rating >= :minRating) "
        + "AND (:maxRating IS NULL OR p.rating <= :maxRating) "
        + "AND (:minStock IS NULL OR p.stock >= :minStock) "
        + "AND (:maxStock IS NULL OR p.stock <= :maxStock) "
        + "AND (:minWeekSells IS NULL OR p.lastWeekSells >= :minWeekSells) "
        + "AND (:maxWeekSells IS NULL OR p.lastWeekSells <= :maxWeekSells) "
        + "AND (:minNumberRatings IS NULL OR p.numberRatings >= :minNumberRatings) "
        + "AND (:maxNumberRatings IS NULL OR p.numberRatings <= :maxNumberRatings) "
        + "AND (:minTotalSells IS NULL OR p.totalSells >= :minTotalSells) "
        + "AND (:maxTotalSells IS NULL OR p.totalSells <= :maxTotalSells)")
    List<Product> searchProducts(String name, Integer minPrice, Integer maxPrice, List<String> tags, 
        List<ProductType> productTypes, String minSale, String maxSale, String minRating, String maxRating, 
        String minStock, String maxStock, String minWeekSells, String maxWeekSells, String minNumberRatings, 
        String maxNumberRatings, String minTotalSells, String maxTotalSells);

}
