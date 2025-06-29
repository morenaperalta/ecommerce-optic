package com.irisandco.ecommerce_optic.product;

import com.irisandco.ecommerce_optic.category.Category;
import com.irisandco.ecommerce_optic.category.CategoryMapper;
import com.irisandco.ecommerce_optic.category.CategoryResponseShort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository PRODUCT_REPOSITORY;

    public ProductService(ProductRepository productRepository) {
        this.PRODUCT_REPOSITORY = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = PRODUCT_REPOSITORY.findAll();

        return products.stream()
                .map(product -> {
                    // 1. Obtener las categorías del producto
                    List<Category> productCategories = product.getCategories();

                    // 2. Convertirlas en CategoryResponseShort usando el mapper
                    List<CategoryResponseShort> shortCategories = productCategories.stream()
                            .map(category -> CategoryMapper.toDtoShort(category)
)
                            .collect(Collectors.toList());

                    // 3. Crear el DTO final con el producto y sus categorías resumidas
                    return ProductMapper.toDto(product, shortCategories);
                })
                .collect(Collectors.toList());
    }




}
