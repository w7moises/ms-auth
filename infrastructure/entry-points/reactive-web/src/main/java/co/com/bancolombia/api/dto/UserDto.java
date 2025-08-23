package co.com.bancolombia.api.dto;

import java.math.BigDecimal;
import java.util.Date;

public record UserDto(
        String name,
        String lastName,
        Date birthDate,
        String address,
        String cellphone,
        String email,
        BigDecimal salary
) {
}
