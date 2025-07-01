package com.irisandco.ecommerce_optic.product;

import com.irisandco.ecommerce_optic.category.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository PRODUCT_REPOSITORY;
    private final CategoryService CATEGORY_SERVICE;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.PRODUCT_REPOSITORY = productRepository;
        this.CATEGORY_SERVICE = categoryService;
    }

    public List<ProductResponse> getAllProducts() {
        return listToDto(PRODUCT_REPOSITORY.findAll());
    }

    private List<ProductResponse> listToDto(List<Product> products) {
        return products.stream().map(product ->  ProductMapper.toDto(product))
                .toList();
        }

    public Product getProductById(Long id) {
        return PRODUCT_REPOSITORY.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    public ProductResponse getProductResponseById(Long id) {
        return ProductMapper.toDto(getProductById(id));
    }

    public ProductResponse createProduct (ProductRequest productRequest) {
        if (PRODUCT_REPOSITORY.existsByName(productRequest.name())) {
            new IllegalArgumentException("There is already a product named " + productRequest.name());
        }

        //Obtener categorías por sus id
        List<Category> categories = productRequest.categoryNames().stream().map(id -> CATEGORY_SERVICE.getCategoryById(id)).toList();

        // Convertir el DTO a entidad con las categorías ya cargadas
        Product product = ProductMapper.toEntity(productRequest, categories);

        // Guardar producto
        Product savedProduct = PRODUCT_REPOSITORY.save(product);

        // Devolver el DTO con las categorías incluidas
        return ProductMapper.toDto(savedProduct);
    }

    public void deleteProduct(Long id) {
        //Verify if the id product exist or throw an exception
        Product product = getProductById(id);
        PRODUCT_REPOSITORY.deleteById(id);
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
    Product product =getProductById(id);
    if (PRODUCT_REPOSITORY.existsByName(productRequest.name())) { new IllegalArgumentException("There is already a product named " + productRequest.name());
    }
    product.setName(productRequest.name());
    product.setPrice(productRequest.price());
    product.setImageUrl(productRequest.imageUrl());
    product.setFeatured(productRequest.featured());

    List<Category> categories = productRequest.categoryNames().stream().map(categoryId -> CATEGORY_SERVICE.getCategoryById(id)).toList();

    product.setCategories(categories);

    // Guardar product
    Product savedProduct = PRODUCT_REPOSITORY.save(product);

    return ProductMapper.toDto(savedProduct);
    }
    }




