package com.irisandco.ecommerce_optic.category;

import com.irisandco.ecommerce_optic.product.ProductResponseShort;

import java.util.List;

public class CategoryMapper {
    public static Category toEntity(CategoryRequest categoryRequest){
        return new Category(categoryRequest.name());
    }

//    public static Category toEntity(CategoryRequest categoryRequest){
//        Category category = new Category();
//        category.setName(categoryRequest.name());
//        return category;
//    }

    public static CategoryResponse toDto(Category category, List<ProductResponseShort> products){
        return new CategoryResponse(category.getId(), category.getName(), products);
    }

    public static CategoryResponseShort toDtoShort(Category category){
        return new CategoryResponseShort(category.getId(), category.getName());
    }
}
