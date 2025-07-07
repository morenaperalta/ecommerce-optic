package com.irisandco.ecommerce_optic.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Category name must not be empty")
        @Size(min = 2, max = 50, message = "Category name must contain min 2 and max 50 characters")
        String name
) {
}
