package com.irisandco.ecommerce_optic.product;

import com.irisandco.ecommerce_optic.category.Category;
import com.irisandco.ecommerce_optic.category.CategoryMapper;
import com.irisandco.ecommerce_optic.category.CategoryResponseShort;

import java.util.List;

public class ProductMapper {
    public static Product toEntity(ProductRequest productRequest, List<Category> categories) {
        return new Product(productRequest.name().trim(), productRequest.price(), productRequest.imageUrl().trim(), productRequest.featured(), categories);
    }

    public static ProductResponse toDto(Product product) {
        List<CategoryResponseShort> categories = product.getCategories().stream().map(category -> CategoryMapper.toDtoShort(category)).toList();

        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getFeatured(), categories);
    }

    public static ProductResponseShort toDtoShort(Product product) { return new ProductResponseShort(product.getId(), product.getName(), product.getPrice(), product.getFeatured());
    }


}
