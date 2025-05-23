package es.dlj.onlinestore.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable;

import es.dlj.onlinestore.domain.Image;
import es.dlj.onlinestore.domain.Order;
import es.dlj.onlinestore.domain.Product;
import es.dlj.onlinestore.domain.ProductTag;
import es.dlj.onlinestore.domain.Review;
import es.dlj.onlinestore.domain.User;
import es.dlj.onlinestore.dto.ImageDTO;
import es.dlj.onlinestore.dto.ProductDTO;
import es.dlj.onlinestore.dto.ProductSimpleDTO;
import es.dlj.onlinestore.dto.ProductTagDTO;
import es.dlj.onlinestore.enumeration.ProductType;
import es.dlj.onlinestore.mapper.ImageMapper;
import es.dlj.onlinestore.mapper.ProductMapper;
import es.dlj.onlinestore.mapper.ProductTagMapper;
import es.dlj.onlinestore.repository.OrderRepository;
import es.dlj.onlinestore.repository.ProductRepository;
import es.dlj.onlinestore.repository.ProductTagRepository;
import es.dlj.onlinestore.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import es.dlj.onlinestore.repository.ReviewRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductTagRepository productTagRepository;

    @Autowired
    private OrderRepository orderRepository;



    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;



    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductTagMapper productTagMapper;

    @Autowired
    private ImageMapper imageMapper;
    
    @PostConstruct
    @Transactional
    public void init() {
        if (productRepository.count()>0) return ;
        // Preload products
        List<Product> productList = List.of(
            productRepository.save(new Product("Laptop Dell XPS 15", 1500.99f, "High-end laptop", ProductType.NEW, 10, 20)),
            productRepository.save(new Product("iPhone 13 Pro", 1200.99f, "Latest Apple smartphone", ProductType.NEW, 15, 10)),
            productRepository.save(new Product("Samsung Galaxy S21", 1000.99f, "Samsung flagship phone", ProductType.RECONDITIONED, 20, 15)),
            productRepository.save(new Product("HP Pavilion 14", 750.99f, "Affordable HP laptop", ProductType.SECONDHAND, 12, 5)),
            productRepository.save(new Product("MacBook Air M1", 999.99f, "Apple M1 laptop", ProductType.NEW, 8, 25)),
            productRepository.save(new Product("PlayStation 5", 499.99f, "Next-gen gaming console", ProductType.NEW, 5, 30)),
            productRepository.save(new Product("Xbox Series X", 499.99f, "Microsoft gaming console", ProductType.RECONDITIONED, 6, 20)),
            productRepository.save(new Product("iPad Air 4", 599.99f, "Apple tablet", ProductType.NEW, 10, 10)),
            productRepository.save(new Product("Kindle Paperwhite", 150.99f, "Amazon e-reader", ProductType.SECONDHAND, 20, 5)),
            productRepository.save(new Product("Sony WH-1000XM4", 350.99f, "Noise-canceling headphones", ProductType.NEW, 15, 25)),
            productRepository.save(new Product("Bose QC35 II", 299.99f, "Wireless headphones", ProductType.RECONDITIONED, 12, 15)),
            productRepository.save(new Product("LG OLED CX 55", 1300.99f, "55-inch OLED TV", ProductType.NEW, 8, 20)),
            productRepository.save(new Product("Samsung QLED Q80T", 1200.99f, "65-inch QLED TV", ProductType.NEW, 10, 10)),
            productRepository.save(new Product("Nikon D3500", 450.99f, "DSLR Camera", ProductType.SECONDHAND, 7, 5)),
            productRepository.save(new Product("Canon EOS M50", 600.99f, "Mirrorless Camera", ProductType.NEW, 5, 20)),
            productRepository.save(new Product("GoPro Hero 9", 400.99f, "Action Camera", ProductType.RECONDITIONED, 10, 15)),
            productRepository.save(new Product("Surface Pro 7", 800.99f, "Microsoft tablet-laptop hybrid", ProductType.NEW, 9, 10)),
            productRepository.save(new Product("Dell UltraSharp 27", 500.99f, "4K Monitor", ProductType.NEW, 11, 25)),
            productRepository.save(new Product("Apple Watch Series 7", 399.99f, "Smartwatch", ProductType.NEW, 14, 20)),
            productRepository.save(new Product("Samsung Galaxy Watch 4", 299.99f, "Smartwatch", ProductType.RECONDITIONED, 13, 15))
        );

        // Preload images
        List<List<String>> productImages = new ArrayList<>();
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));
        productImages.add(List.of("image-01-1.png", "image-01-2.png"));
        productImages.add(List.of("image-02-1.png", "image-02-2.png"));
        productImages.add(List.of("image-03-1.png", "image-03-2.png"));

        // Preload tags
        List<List<ProductTag>> productTagsList = new ArrayList<>();
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("electronics, laptop")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("smartphone, apple")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("smartphone, android")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("laptop, hp")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("laptop, apple")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("gaming, console")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("gaming, console")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("tablet, apple")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("tablet, reader")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("audio, headphones")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("audio, headphones")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("tv, lg")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("tv, samsung")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("camera, nikon")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("camera, canon")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("camera, gopro")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("tablet, microsoft")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("monitor, dell")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("watch, apple")));
        productTagsList.add(productTagMapper.toDomains(transformStringToTags("watch, samsung")));

        // Preload products, tags and images
        User user = userService.findById(1L);
        User user2 = userService.findById(2L);

        

        for (int i = 0; i < productList.size(); i++) {

            for (ProductTag tag : productTagsList.get(i)) {
                
                tag.addProduct(productList.get(i));
                productTagRepository.save(tag);
            }
            productList.get(i).setTags(productTagsList.get(i));
            try {
                for (String productImage : productImages.get(i)) {
                    InputStream stream = getClass().getResourceAsStream("/static/images/preloaded/" + productImage);
                    Image thisImage = imageService.saveImageFromInputStream(stream, productImage);
                    // Image thisImage = imageService.saveFileImageFromPath("src/main/resources/static/images/preloaded/" + productImage);
                    productList.get(i).addImage(thisImage);
                }
            } catch (Exception e) {} //IOException

            if (i < 8) productList.get(i).setSeller(user);
            else productList.get(i).setSeller(user2);
            productRepository.save(productList.get(i));
        }        
    }

    public ProductDTO findDTOById(Long id) {
        return productMapper.toDTO(findById(id));
    }

    Product findById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO updatedProductDTO, List<MultipartFile> imagesVal, String tagsVal) {
        Product product = findById(id);
        product.setName(updatedProductDTO.name());
        product.setPrice(updatedProductDTO.price());
        product.setDescription(updatedProductDTO.description());
        product.setProductType(updatedProductDTO.productType());
        product.setStock(updatedProductDTO.stock());
        product.setSale(updatedProductDTO.sale());

        if (imagesVal != null && imagesVal.size() >= 1) {
            try {
                if (product.getImages() != null) {
                    for (Image image : product.getImages()) {
                        imageService.delete(image);
                    }
                }
                imageService.saveImagesInProduct(product, imagesVal);
            } catch (IOException e) {
                throw new NoSuchElementException("Error loading images"); 
            }
        }   

        List<ProductTag> oldTags = product.getTags();        
        Product savedProduct = save(product);
        savedProduct.getTags().clear();
        List<ProductTag> newTags = productTagMapper.toDomains(transformStringToTags(tagsVal));
        for (ProductTag tag : oldTags) {
            if (!newTags.contains(tag)) {
                tag.removeProduct(product);
                saveTag(tag);
            }
        }
    
        for (ProductTag tag : newTags) {
            savedProduct.addTag(tag);
            if (!oldTags.contains(tag)) {
                tag.addProduct(savedProduct);
                saveTag(tag);
            }
        }

        return productMapper.toDTO(save(savedProduct));
    }

    @Transactional
    public List<ProductTagDTO> transformStringToTags(String tagsAsString){
        List<ProductTag> tagList = new ArrayList<>();
        for (String tag: tagsAsString.split(",")){
            tag = tag.trim();
            if (tag.isEmpty()) continue;
            ProductTag productTag;
            if (productTagRepository.existsByName(tag)) {
                productTag = productTagRepository.findByName(tag);
            } else {
                productTag = new ProductTag(tag);
                productTag = productTagRepository.save(productTag);
            }
            tagList.add(productTag);
        }
        return productTagMapper.toDTOs(tagList);
    }

    public List<Map<String, Object>> getAllTags() {
        // Get all tags and the number of products that have that tag
        List<Map<String, Object>> tags = new ArrayList<>();
        for (ProductTag tag : productTagRepository.findAll()) {
            tags.add(Map.of("name", tag.getName(), "count", tag.getProducts().size()));
        }
        // Sort by count descending
        tags.sort((a, b) -> - ((Integer) a.get("count")).compareTo((Integer) b.get("count")));
        return tags;
    }

    public List<ProductTagDTO> getAllTagsDTO() {
        return productTagMapper.toDTOs(productTagRepository.findAll());
    }

    public List<Map<String, Object>> getAllProductTypesAndCount(ProductType selected) {
        // Get all product types and the number of products that have that type
        List<Map<String, Object>> productTypesList = new ArrayList<>();
        for (ProductType type : ProductType.values()) {
            productTypesList.add(Map.of("name", type.toString(), 
                                        "count", productRepository.countByProductType(type),
                                        "selected", (selected != null && type.equals(selected))));
        }
        return productTypesList;
    }

    public List<Map<String, Object>> getAllProductTypesAndCount() {
        return getAllProductTypesAndCount(null);
    }

    public Collection<ProductSimpleDTO> findAllDTOs() {
        return productMapper.toDTOs(productRepository.findAll());
    }

    public Page<ProductDTO> findAllDTOs(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(productMapper::toDTO);
    }

    Collection<Product> findAll() {
        return productRepository.findAll();
    }

    public Collection<ProductSimpleDTO> findAllDTOsByProductType(ProductType type) {
        return productMapper.toDTOs(productRepository.findByProductType(type));
    }

    public Page<ProductDTO> findAllDTOsByProductType(ProductType type, Pageable pageable) {
        Page<Product> products = productRepository.findByProductType(type, pageable);
        return products.map(productMapper::toDTO);
    }

    public Collection<ProductSimpleDTO> findAllDTOsByNameContaining(String name) {
        return productMapper.toDTOs(productRepository.findByNameContaining(name));
    }

    public Page<ProductDTO> findAllDTOsByNameContaining(String name, Pageable pageable) {
        Page<Product> products = productRepository.findByNameContaining(name, pageable);
        return products.map(productMapper::toDTO);
    }

    public ProductDTO findDTOById(long id) {
        return productMapper.toDTO(productRepository.findById(id).orElseThrow());
    }

    public Page<ProductSimpleDTO> findAllDTOsBy(String name, Integer minPrice, Integer maxPrice, List<String> tags, List<String> productTypeStrings, Pageable pageable) {
        // Transform productTypeStrings to ProductType
        List<ProductType> productTypes = null;
        if (productTypeStrings != null) {
            productTypes = new ArrayList<>();
            for (String type : productTypeStrings) {
                productTypes.add(ProductType.valueOf(type));
            }
        }

        if (tags != null && tags.isEmpty()) {
            tags = null;
        }

        return productRepository
            .searchProducts(name, minPrice, maxPrice, tags, productTypes, pageable)
            .map(productMapper::toSimpleDTO);
    }

    public Collection<ProductSimpleDTO> getBestSellers() {
        return productMapper.toDTOs(productRepository.findTop10ByOrderByTotalSellsDesc());
    }

    public Collection<ProductSimpleDTO> getBestSales() {
        return productMapper.toDTOs(productRepository.findTop10ByOrderBySaleDesc());
    }

    public Collection<ProductSimpleDTO> getLowStock(int stock) {
        return productMapper.toDTOs(productRepository.findByStockLessThan(stock));
    }

    Product save(Product product) {
        return productRepository.save(product);
    }

    ProductTag saveTag(ProductTag tag) {
        return productTagRepository.save(tag);
    }

    public ProductTagDTO saveTagDTO(ProductTagDTO tagDTO){
        ProductTag tag = productTagMapper.toDomain(tagDTO);
        return productTagMapper.toDTO(saveTag(tag));
    }

    public boolean isProductOwner(Long productId) {
        try {
            User user = userService.getLoggedUser();
            if (user == null) return false;
            if (user.getRoles().contains("ADMIN")) return true;
            Product product = findById(productId); 
            return user.getId() == product.getSeller().getId();
        } catch (Exception e) {
            return false;
        }
    }

    public Collection<ProductTagDTO> findAllTagsDTOs() {
        return productTagMapper.toDTOs(productTagRepository.findAll());
    }

    @Transactional
    public ProductDTO saveProduct(ProductDTO newProductDTO, List<MultipartFile> imagesVal, String tagsVal) {
        Product product = productMapper.toDomain(newProductDTO);
        User user = userService.getLoggedUser();
        product.setSeller(user);
        product.setTags(productTagMapper.toDomains(transformStringToTags(tagsVal))); 
        if (product.getReviews() == null) product.setReviews(new ArrayList<Review>());

        Product savedProduct = save(product);
    
        try {
            if (imagesVal != null && imagesVal.size() > 1) {
                imageService.saveImagesInProduct(savedProduct, imagesVal);
            }
        } catch (IOException e) {
            throw new NoSuchElementException("Error loading images"); 
        }

        List<ProductTag> tags = savedProduct.getTags();
        for (ProductTag tag : tags) {
            tag.addProduct(product);
        }

        
        user.addProductForSale(savedProduct);
        userService.save(user);
        savedProduct = save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Transactional
    public ProductDTO deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null){
            deepDeleteImages(product);
            deepDeleteReviews(product);
            deepDeleteTags(product);
            deepDeleteSeller(product);
            deepDeleteOrders(product);
            productRepository.delete(product);
        }
        return productMapper.toDTO(product);
    }

    @Transactional
    private void deepDeleteImages(Product product) {
        List<Image> images = new ArrayList<>(product.getImages());
        for (Image image : images) {
            imageService.delete(image);
        }
        product.clearImages();
    }

    @Transactional
    private void deepDeleteSeller(Product product) {
        User seller = product.getSeller();
        if (seller != null) {
            seller.removeProductFromSales(product);
            userRepository.save(seller);
        }
    }

    @Transactional
    private void deepDeleteOrders(Product product) {
        List<Order> orders = orderRepository.findByProductsContaining(product);
        for (Order order : orders) {
            order.removeProduct(product);
            orderRepository.save(order);
        }
    }

    @Transactional
    private void deepDeleteTags(Product product) {
        List<ProductTag> tags = product.getTags();
        for (ProductTag tag : tags) {
            tag.getProducts().remove(product);
            productTagRepository.save(tag);
        }
    }

    @Transactional
    private void deepDeleteReviews(Product product) {
        List<Review> reviews = new ArrayList<>(product.getReviews());
        for (Review review : reviews) {
            User owner = review.getOwner();
            if (owner != null) {
                owner.removeReview(review);
                userService.save(owner);
            }
            review.setProduct(null);
            product.getReviews().remove(review);
            reviewRepository.delete(review);
        }
    }

    public Page<ProductDTO> getAllProductsByUserId(Long id, Pageable pageable) {
        Page<Product> products = productRepository.findAllBySellerId(id, pageable);
        return products.map(productMapper::toDTO);
    }

    public ImageDTO addImage(Long id, MultipartFile image) throws IOException {
        Image savedImage = imageMapper.toDomain(imageService.saveFileImage(image));
        Product product = productRepository.findById(id).orElseThrow();
        product.addImage(savedImage);
        productRepository.save(product);
        return imageMapper.toDTO(savedImage);
    }

    public ImageDTO deleteImage(ImageDTO imageDTO, Long id) {
        Product product = findById(id);
        product.removeImage(imageDTO.id());
        productRepository.save(product);
        return imageService.delete(imageDTO);
    }  
}