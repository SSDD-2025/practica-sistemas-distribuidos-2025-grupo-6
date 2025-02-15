package es.dlj.onlinestore.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import es.dlj.onlinestore.model.Product;
import es.dlj.onlinestore.model.ProductType;
import es.dlj.onlinestore.repository.ProductRepository;
import jakarta.annotation.PostConstruct;

@Service
public class ProductService {

    @Autowired
    private ProductRepository products;

    @PostConstruct
    public void init() {
        /*
        products.save(new Product("product1", 10.0f, "description1", Product.productType.NEW, 10, new LinkedList<>()));
        products.save(new Product("product2", 20.0f, "description2", Product.productType.NEW, 20, new LinkedList<>()));
        products.save(new Product("product3", 30.0f, "description3", Product.productType.NEW, 30, new LinkedList<>()));
        products.save(new Product("product4", 40.0f, "description4", Product.productType.RECONDITIONED, 40, new LinkedList<>()));
        products.save(new Product("product5", 50.0f, "description5", Product.productType.SECONDHAND, 50, new LinkedList<>()));
        products.save(new Product("product6", 60.0f, "description6", Product.productType.NEW, 60, new LinkedList<>()));
        */

        products.save(new Product("Laptop Dell XPS 15", 1500f, "High-end laptop", ProductType.NEW, 10, Arrays.asList("electronics", "laptop")));
        products.save(new Product("iPhone 13 Pro", 1200f, "Latest Apple smartphone", ProductType.NEW, 15, Arrays.asList("smartphone", "apple")));
        products.save(new Product("Samsung Galaxy S21", 1000f, "Samsung flagship phone", ProductType.NEW, 20, Arrays.asList("smartphone", "android")));
        products.save(new Product("HP Pavilion 14", 750f, "Affordable HP laptop", ProductType.NEW, 12, Arrays.asList("laptop", "hp")));
        products.save(new Product("MacBook Air M1", 999f, "Apple M1 laptop", ProductType.NEW, 8, Arrays.asList("laptop", "apple")));
        products.save(new Product("PlayStation 5", 499f, "Next-gen gaming console", ProductType.NEW, 5, Arrays.asList("gaming", "console")));
        products.save(new Product("Xbox Series X", 499f, "Microsoft gaming console", ProductType.NEW, 6, Arrays.asList("gaming", "console")));
        products.save(new Product("iPad Air 4", 599f, "Apple tablet", ProductType.NEW, 10, Arrays.asList("tablet", "apple")));
        products.save(new Product("Kindle Paperwhite", 150f, "Amazon e-reader", ProductType.NEW, 20, Arrays.asList("tablet", "reader")));
        products.save(new Product("Sony WH-1000XM4", 350f, "Noise-canceling headphones", ProductType.NEW, 15, Arrays.asList("audio", "headphones")));
        products.save(new Product("Bose QC35 II", 299f, "Wireless headphones", ProductType.NEW, 12, Arrays.asList("audio", "headphones")));
        products.save(new Product("LG OLED CX 55", 1300f, "55-inch OLED TV", ProductType.NEW, 8, Arrays.asList("tv", "lg")));
        products.save(new Product("Samsung QLED Q80T", 1200f, "65-inch QLED TV", ProductType.NEW, 10, Arrays.asList("tv", "samsung")));
        products.save(new Product("Nikon D3500", 450f, "DSLR Camera", ProductType.NEW, 7, Arrays.asList("camera", "nikon")));
        products.save(new Product("Canon EOS M50", 600f, "Mirrorless Camera", ProductType.NEW, 5, Arrays.asList("camera", "canon")));
        products.save(new Product("GoPro Hero 9", 400f, "Action Camera", ProductType.NEW, 10, Arrays.asList("camera", "gopro")));
        products.save(new Product("Surface Pro 7", 800f, "Microsoft tablet-laptop hybrid", ProductType.NEW, 9, Arrays.asList("tablet", "microsoft")));
        products.save(new Product("Dell UltraSharp 27", 500f, "4K Monitor", ProductType.NEW, 11, Arrays.asList("monitor", "dell")));
        products.save(new Product("Apple Watch Series 7", 399f, "Smartwatch", ProductType.NEW, 14, Arrays.asList("watch", "apple")));
        products.save(new Product("Samsung Galaxy Watch 4", 299f, "Smartwatch", ProductType.NEW, 13, Arrays.asList("watch", "samsung")));
        products.save(new Product("Apple Watch Series 7", 399f, "Smartwatch", ProductType.NEW, 14, Arrays.asList("watch", "apple")));
        products.save(new Product("Samsung Galaxy Watch 4", 299f, "Smartwatch", ProductType.NEW, 13, Arrays.asList("watch", "samsung")));


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

    public List<Product> findAll(Example<Product> example) {
        return products.findAll(example);
    }

}
