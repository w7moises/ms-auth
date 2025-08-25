package co.com.bancolombia.model.role;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleTest {
    private static final Role ROLE_ADMIN = Role.builder()
            .id(1L).name("ADMIN").description("Administrador").build();

    private static final Role ROLE_AUDITOR = Role.builder()
            .id(2L).name("AUDITOR").description("Auditor del sistema").build();

    @Test
    void shouldBuild() {
        assertEquals(1L, ROLE_ADMIN.getId());
        assertEquals("ADMIN", ROLE_ADMIN.getName());
        assertEquals("Administrador", ROLE_ADMIN.getDescription());
    }

    @Test
    void settersAndGetters() {
        Role r = new Role();
        r.setId(ROLE_AUDITOR.getId());
        r.setName(ROLE_AUDITOR.getName());
        r.setDescription(ROLE_AUDITOR.getDescription());
        assertEquals(2L, r.getId());
        assertEquals("AUDITOR", r.getName());
        assertEquals("Auditor del sistema", r.getDescription());
    }

    @Test
    void useToBuilder() {
        Role mod = ROLE_ADMIN.toBuilder().description("Admin global").build();
        assertEquals(1L, mod.getId());
        assertEquals("ADMIN", mod.getName());
        assertEquals("Admin global", mod.getDescription());
    }
}
