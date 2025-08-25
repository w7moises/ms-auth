package co.com.bancolombia.api.utils;

import co.com.bancolombia.api.dto.CreateUserDto;
import co.com.bancolombia.api.dto.EditUserDto;
import co.com.bancolombia.model.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MockData {
    public static final String USERS_BASE = "/api/v1/users";

    public static final User USER_MOCK = User.builder()
            .id(1L)
            .name("Moises")
            .lastName("Molina")
            .documentNumber("73727173")
            .birthDate(LocalDate.now())
            .address("Calle ayacucho")
            .cellphone("982389811")
            .email("w7@gmail.com")
            .salary(new BigDecimal("2345"))
            .build();

    public static final User USER_MOCK2 = User.builder()
            .id(2L)
            .name("Walter")
            .lastName("Molina")
            .documentNumber("73727173")
            .birthDate(LocalDate.now())
            .address("Av. puquina 115")
            .cellphone("666423665")
            .email("wmolina@gmail.com")
            .salary(new BigDecimal("1234"))
            .build();

    public static final User USER_MOCK_UPDATED = User.builder()
            .id(2L)
            .name("Walter")
            .lastName("UPDATE")
            .documentNumber("73727173")
            .birthDate(LocalDate.of(1999, 5, 22))
            .address("Av. puquina 115")
            .cellphone("666423665")
            .email("wmolina@gmail.com")
            .salary(new BigDecimal("1234"))
            .build();

    public static final CreateUserDto CREATE_USER_DTO = new CreateUserDto(
            "Walter", "Molina", "73728131", LocalDate.of(1999, 5, 22),
            "Av. puquina 115", "666423665", "wmolina@gmail.com",
            new BigDecimal("1234")
    );

    public static final EditUserDto EDIT_USER_DTO = new EditUserDto(
            "Moises", "Molina", "73728131", LocalDate.of(1992, 2, 2),
            "Calle ayacucho", "982389811", "w7@gmail.com",
            new BigDecimal("2345")
    );
}
