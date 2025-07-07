package com.irisandco.ecommerce_optic.item;

public record ItemResponse (
        Long id,
        Long productId,
        String productName,
        Integer quantity,
        Double productPrice
){
}
