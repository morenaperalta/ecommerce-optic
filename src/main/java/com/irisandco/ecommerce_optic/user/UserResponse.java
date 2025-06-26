package com.irisandco.ecommerce_optic.user;

public record UserResponse(
        Long id,
        String username,
        String email
) {
}
