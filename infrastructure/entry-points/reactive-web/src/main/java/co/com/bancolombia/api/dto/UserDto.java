package co.com.bancolombia.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserDto(
        Long id,
        String name,
        String lastName,
        String documentNumber,
        LocalDate birthDate,
        String address,
        String cellphone,
        String email,
        BigDecimal salary
) {
}
