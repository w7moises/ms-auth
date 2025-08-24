package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EditUserDto(
        @NotBlank(message = "{name.required}")
        String name,

        @NotBlank(message = "{lastName.constraint}")
        String lastName,

        @NotNull(message = "{birthDate.required}")
        @Past(message = "{birthDate.past}")
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate birthDate,

        String address,

        String cellphone,

        @NotBlank(message = "{email.required}")
        @Email(message = "{email.format}")
        String email,

        @NotNull(message = "{salary.required}")
        @DecimalMin(value = "0.0", inclusive = false, message = "{salary.min}")
        @DecimalMax(value = "1500000.0", inclusive = false, message = "{salary.max}")
        BigDecimal salary
) {
}
