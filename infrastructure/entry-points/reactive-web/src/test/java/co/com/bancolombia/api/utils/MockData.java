package co.com.bancolombia.api.utils;

import co.com.bancolombia.api.dto.CreateUserDto;
import co.com.bancolombia.api.dto.EditUserDto;
import co.com.bancolombia.model.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MockData {
    public static final String USERS_BASE = "/api/v1/users";

    public static User userMock() {
        return User.builder()
                .id(1L)
                .name("Moises")
                .lastName("Molina")
                .birthDate(LocalDate.of(1992, 2, 2))
                .address("Calle ayacucho")
                .cellphone("982389811")
                .email("w7@gmail.com")
                .salary(new BigDecimal("2345"))
                .build();
    }

    public static User userMock2() {
        return User.builder()
                .id(2L)
                .name("Walter")
                .lastName("Molina")
                .birthDate(LocalDate.of(1999, 5, 22))
                .address("Av. puquina 115")
                .cellphone("666423665")
                .email("wmolina@gmail.com")
                .salary(new BigDecimal("1234"))
                .build();
    }

    public static User userUpdated() {
        return User.builder()
                .id(1L)
                .name("Moises")
                .lastName("Updated")
                .birthDate(LocalDate.of(1992, 2, 2))
                .address("Calle ayacucho")
                .cellphone("982389811")
                .email("w7@gmail.com")
                .salary(new BigDecimal("2345"))
                .build();
    }

    public static CreateUserDto createUserDto() {
        return new CreateUserDto(
                "Walter", "Molina", LocalDate.of(1999, 5, 22),
                "Av. puquina 115", "666423665", "wmolina@gmail.com",
                new BigDecimal("1234")
        );
    }

    public static EditUserDto editUserDtoUpdated() {
        return new EditUserDto(
                "Moises", "Updated", LocalDate.of(1992, 2, 2),
                "Calle ayacucho", "982389811", "w7@gmail.com",
                new BigDecimal("2345")
        );
    }
}
