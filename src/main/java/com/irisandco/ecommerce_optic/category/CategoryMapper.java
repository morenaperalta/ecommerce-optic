package com.irisandco.ecommerce_optic.category;

import com.irisandco.ecommerce_optic.product.ProductMapper;
import com.irisandco.ecommerce_optic.product.ProductResponseShort;

import java.util.List;

public class CategoryMapper {
    public static Category toEntity(CategoryRequest categoryRequest){
        return new Category(categoryRequest.name().trim());
    }

    public static CategoryResponse toDto(Category category){
        List<ProductResponseShort> products = category.getProducts().stream()
                            .map(ProductMapper::toDtoShort)
                            .toList();
        return new CategoryResponse(category.getId(), category.getName(), products);
    }

    public static CategoryResponseShort toDtoShort(Category category){
        return new CategoryResponseShort(category.getId(), category.getName());
    }
}
