package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EditUserDto(
        @NotBlank(message = "Los nombres son necesario")
        String name,

        @NotBlank(message = "Los apellidos son necesarios")
        String lastName,

        @NotNull(message = "La fecha de nacimiento es necesaria")
        @Past(message = "La fecha de nacimiento debe estar en el pasado")
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate birthDate,

        String address,

        String cellphone,

        @NotBlank(message = "El correo electrónico es necesario")
        @Email(message = "El correo electrónico debe tener un buen formato")
        String email,

        @NotNull(message = "El salario es necesario")
        @DecimalMin(value = "0.0", inclusive = false, message = "El salario no puede ser negativo")
        @DecimalMax(value = "1500000.0", inclusive = false, message = "El salario no puede superar 1,500,000")
        BigDecimal salary
) {
}
