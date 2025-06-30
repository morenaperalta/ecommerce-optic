package com.irisandco.ecommerce_optic.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CartRequest(
        @NotBlank
        @Positive
        Integer quantity
) {
}
