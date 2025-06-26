package com.irisandco.ecommerce_optic.product;

import com.irisandco.ecommerce_optic.category.CategoryResponseShort;

import java.util.List;

public record ProductResponse(
    Long id,
    String name,
    Double price,
    String imageUrl,
    Boolean featured,
    List<CategoryResponseShort> categories
) {
}
