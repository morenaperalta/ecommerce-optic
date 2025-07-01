package com.irisandco.ecommerce_optic.cart;

import com.irisandco.ecommerce_optic.item.ItemResponse;

import java.util.List;

public class CartMapper {
    public static CartResponse toDto(Cart cart, List<ItemResponse> items){
        return new CartResponse(cart.getId(), cart.getUser().getId(), items, cart.getTotalPrice());
    }
}
