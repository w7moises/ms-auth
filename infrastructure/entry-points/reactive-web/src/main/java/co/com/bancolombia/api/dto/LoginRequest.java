package co.com.bancolombia.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "{email.format}")
        @NotBlank(message = "{email.required}")
        String email,

        @NotBlank(message = "{password.required}")
        String password
) {
}
