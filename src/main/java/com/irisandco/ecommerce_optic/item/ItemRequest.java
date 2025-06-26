package com.irisandco.ecommerce_optic.item;

import jakarta.validation.constraints.Positive;
import org.springframework.lang.NonNull;

public record ItemRequest(
        Long productId,
        @NonNull
        @Positive(message = "Item quantity must be positive")
        Integer quantity,
        Long userId
) {
}
