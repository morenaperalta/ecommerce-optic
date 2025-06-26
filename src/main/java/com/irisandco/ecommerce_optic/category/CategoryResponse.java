package com.irisandco.ecommerce_optic.category;

public record CategoryResponse(
        Long id,
        String name,
        List<ProductResponseShort> products
) {
}
