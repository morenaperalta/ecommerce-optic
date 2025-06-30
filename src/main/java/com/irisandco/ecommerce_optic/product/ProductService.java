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

    private ProductResponse toDto(Product product) {
        List<CategoryResponseShort> shortCategories = product.getCategories().stream().map(category -> CategoryMapper.toDtoShort(category)).toList();
        return ProductMapper.toDto(product, shortCategories);
    }

    private List<ProductResponse> listToDto(List<Product> products) {
        return products.stream().map(product ->  this.toDto(product))
                .toList();
        }

    public Product getProductById(Long id) {
        return PRODUCT_REPOSITORY.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    public ProductResponse getProductResponseById(Long id) {
        return toDto(getProductById(id));
    }

    public ProductResponse saveProduct (ProductRequest productRequest) {
        if (PRODUCT_REPOSITORY.existsByName(productRequest.name())) {
            new IllegalArgumentException("There is already a product named " + productRequest.name());
        }

        //Obtener categorías por sus id
        List<Category> categories = productRequest.categoryIds().stream().map(id -> CATEGORY_SERVICE.getById(id)).toList();

        // Convertir el DTO a entidad con las categorías ya cargadas
        Product product = ProductMapper.toEntity(productRequest, categories);

        // Guardar producto
        Product savedProduct = PRODUCT_REPOSITORY.save(product);

        // Mapear categorías para la respuesta
        List<CategoryResponseShort> shortCategories = categories.stream().map(category -> CategoryMapper.toDtoShort(category)).toList();

        // Devolver el DTO con las categorías incluidas
        return ProductMapper.toDto(savedProduct,shortCategories);
    }


    }




