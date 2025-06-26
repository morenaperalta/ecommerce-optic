package com.irisandco.ecommerce_optic.category;

import org.springframework.lang.NonNull;

public record CategoryRequest(
        @NonNull
        String name
) {
}
