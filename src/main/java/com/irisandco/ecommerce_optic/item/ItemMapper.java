package com.irisandco.ecommerce_optic.item;

import com.irisandco.ecommerce_optic.product.Product;

public class ItemMapper {
    public static Item toEntity(ItemRequest itemRequest, Product product, Cart cart){
        return new Item(itemRequest.quantity(), product, cart);
    }

    public static ItemResponse toDto(Item item, Product product){
        return new ItemResponse(item.getId(), product.getId(), product.getName(), item.getQuantity(), product.getPrice());
    }
}
