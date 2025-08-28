package co.com.bancolombia.api.dto;

public record LoginResponse(
        String token,
        String email
) {
}
