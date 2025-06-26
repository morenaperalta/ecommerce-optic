package com.irisandco.ecommerce_optic.product;

public record ProductResponseShort(
        Long id,
        String name,
        Double price,
        Boolean featured
) {
}
