package com.irisandco.ecommerce_optic.product;

import com.irisandco.ecommerce_optic.category.Category;
import com.irisandco.ecommerce_optic.category.CategoryRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProductRequest(
        @NotBlank
        @Size(min = 2, max = 50, message = "Username must contain min 2 and max 50 characters")
        String name,
        @Positive(message = "Price must be positive")
        Double price,
        String imageUrl,
        Boolean featured,
        List<CategoryRequest> categories
) {
}
