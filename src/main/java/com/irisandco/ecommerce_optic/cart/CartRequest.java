package com.irisandco.ecommerce_optic.cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CartRequest(
        @Positive(message = "Minimum quantity for each product in the same transaction is 1")
        @Max(value = 20, message = "Maximum quantity for each product in the same transaction is 20")
        Integer quantity
) {
}
