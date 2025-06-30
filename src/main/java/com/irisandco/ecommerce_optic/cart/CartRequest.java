package com.irisandco.ecommerce_optic.cart;

import jakarta.validation.constraints.NotBlank;

public record CartRequest(
        @NotBlank
        Integer quantity
) {
}
