package es.dlj.onlinestore.service;

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
        products.save(new Product("product1", 10.0f, "description1", Product.productType.NEW, 10));
        products.save(new Product("product2", 20.0f, "description2", Product.productType.NEW, 20));
        products.save(new Product("product3", 30.0f, "description3", Product.productType.NEW, 30));
        products.save(new Product("product4", 40.0f, "description4", Product.productType.NEW, 40));
        products.save(new Product("product5", 50.0f, "description5", Product.productType.NEW, 50));
    }

    public List<Product> getAllProducts() {
        return products.findAll();
    }

    public Product getProduct(long id) {
        return products.findById(id).get();
    }

}
