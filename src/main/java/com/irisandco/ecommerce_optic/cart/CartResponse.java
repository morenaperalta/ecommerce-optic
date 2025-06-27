package com.irisandco.ecommerce_optic.cart;

import com.irisandco.ecommerce_optic.item.ItemResponse;

import java.util.List;

public record CartResponse(
        Long id,
        Long userId,
        List<ItemResponse> items,
        Double totalPrice
) {
}
