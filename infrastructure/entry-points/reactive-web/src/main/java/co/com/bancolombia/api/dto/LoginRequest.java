package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email
        @NotBlank(message = "{email.required}")
        String email,

        @NotBlank(message = "{password.required}")
        String password
) {
}
