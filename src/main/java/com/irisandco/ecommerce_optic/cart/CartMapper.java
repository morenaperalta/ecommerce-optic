package com.irisandco.ecommerce_optic.cart;

import com.irisandco.ecommerce_optic.item.ItemMapper;
import com.irisandco.ecommerce_optic.item.ItemResponse;

import java.util.List;

public class CartMapper {
    public static CartResponse toDto(Cart cart){
        List <ItemResponse> itemsResponse = cart.getItems().stream()
                .map((item) -> ItemMapper.toDto(item))
                .toList();
        return new CartResponse(cart.getId(), cart.getUser().getId(), itemsResponse, cart.getTotalPrice());
    }
}
