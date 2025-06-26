package com.irisandco.ecommerce_optic.category;

import com.irisandco.ecommerce_optic.product.ProductResponseShort;
import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        List<ProductResponseShort> products
) {
}
