package com.irisandco.ecommerce_optic.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest (
        @NotBlank
        @Size(min = 2, max = 50, message = "Username must contain min 2 and max 50 characters")
        String username,
        @NotBlank
        @Size(min = 2, max = 50, message = "Email must contain min 2 and max 50 characters")
        @Email
        String email,
        @NotBlank
        @Size(min = 12, max = 50, message = "Password must contain min 12 and max 50 characters")
        String password
) {

}
