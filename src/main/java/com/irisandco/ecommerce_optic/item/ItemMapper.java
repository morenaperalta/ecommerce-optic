package com.irisandco.ecommerce_optic.item;

import com.irisandco.ecommerce_optic.product.Product;

public class ItemMapper {

    public static ItemResponse toDto(Item item){
        Product product = item.getProduct();
        return new ItemResponse(item.getId(), product.getId(), product.getName(), item.getQuantity(), product.getPrice());
    }
}
