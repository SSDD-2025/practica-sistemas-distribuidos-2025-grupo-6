package es.dlj.onlinestore.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.repository.ProductRepository;
import jakarta.annotation.PostConstruct;

@Service
public class ProductService {

    @Autowired
    private ProductRepository products;

    @PostConstruct
    public void init() {
        products.save(new Product("product1", 10.0f, "description1", Product.productType.NEW, 10, new LinkedList<>()));
        products.save(new Product("product2", 20.0f, "description2", Product.productType.NEW, 20, new LinkedList<>()));
        products.save(new Product("product3", 30.0f, "description3", Product.productType.NEW, 30, new LinkedList<>()));
        products.save(new Product("product4", 40.0f, "description4", Product.productType.RECONDITIONED, 40, new LinkedList<>()));
        products.save(new Product("product5", 50.0f, "description5", Product.productType.SECONDHAND, 50, new LinkedList<>()));
    }

    public List<Product> getAllProducts() {
        return products.findAll();
    }

    public Product getProduct(long id) {
        return products.findById(id).get();
    }

    public List<Product> searchProducts(String query, Float minPrice, Float maxPrice) {
        System.out.println("Query: " + query);
        System.out.println("MinPrice: " + minPrice);
        System.out.println("MaxPrice: " + maxPrice);
        if (query != null) {
            return products.findByNameContainingIgnoreCase(query);
        } else if (minPrice != null && maxPrice != null) {
            return products.findByPriceBetween(minPrice, maxPrice);
        }
        return products.findAll();
    }

}
