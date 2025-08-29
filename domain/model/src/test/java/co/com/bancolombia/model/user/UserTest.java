package co.com.bancolombia.model.user;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    private static final User USER_WALTER = User.builder()
            .id(10L)
            .name("Walter")
            .lastName("Molina")
            .documentNumber("73727173")
            .birthDate(LocalDate.of(1992, 2, 2))
            .address("Calle Ayacucho")
            .cellphone("999888777")
            .email("walter@acme.com")
            .salary(new BigDecimal("2500.00"))
            .roleId(1L)
            .build();

    private static final User USER_MOISES = User.builder()
            .id(11L)
            .name("Moises")
            .lastName("Molina")
            .documentNumber("12345678")
            .birthDate(LocalDate.of(1999, 5, 22))
            .address("Av. Puquina 115")
            .cellphone("988776655")
            .email("moises@acme.com")
            .salary(new BigDecimal("1800.00"))
            .roleId(2L)
            .build();

    @Test
    void shouldBuild() {
        assertEquals(10L, USER_WALTER.getId());
        assertEquals("Walter", USER_WALTER.getName());
        assertEquals("Molina", USER_WALTER.getLastName());
        assertEquals("73727173", USER_WALTER.getDocumentNumber());
        assertEquals(LocalDate.of(1992, 2, 2), USER_WALTER.getBirthDate());
        assertEquals("Calle Ayacucho", USER_WALTER.getAddress());
        assertEquals("999888777", USER_WALTER.getCellphone());
        assertEquals("walter@acme.com", USER_WALTER.getEmail());
        assertEquals(new BigDecimal("2500.00"), USER_WALTER.getSalary());
        assertEquals(1L, USER_WALTER.getRoleId());
    }

    @Test
    void shouldUseSettersAndGetters() {
        User u = new User();
        u.setId(USER_MOISES.getId());
        u.setName(USER_MOISES.getName());
        u.setLastName(USER_MOISES.getLastName());
        u.setDocumentNumber(USER_MOISES.getDocumentNumber());
        u.setBirthDate(USER_MOISES.getBirthDate());
        u.setAddress(USER_MOISES.getAddress());
        u.setCellphone(USER_MOISES.getCellphone());
        u.setEmail(USER_MOISES.getEmail());
        u.setSalary(USER_MOISES.getSalary());
        u.setRoleId(USER_MOISES.getRoleId());

        assertEquals(11L, u.getId());
        assertEquals("Moises", u.getName());
        assertEquals("Molina", u.getLastName());
        assertEquals("12345678", u.getDocumentNumber());
        assertEquals(LocalDate.of(1999, 5, 22), u.getBirthDate());
        assertEquals("Av. Puquina 115", u.getAddress());
        assertEquals("988776655", u.getCellphone());
        assertEquals("moises@acme.com", u.getEmail());
        assertEquals(new BigDecimal("1800.00"), u.getSalary());
        assertEquals(2L, u.getRoleId());
    }

    @Test
    void shouldUseToBuilder() {
        var mod = USER_WALTER.toBuilder()
                .salary(new BigDecimal("3000.00"))
                .roleId(2L)
                .build();
        assertEquals(10L, mod.getId());
        assertEquals(new BigDecimal("3000.00"), mod.getSalary());
        assertEquals(2L, mod.getRoleId());
    }
}
