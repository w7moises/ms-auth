package co.com.bancolombia.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    private Long id;
    private String name;
    @Column("last_name")
    private String lastName;
    @Column("document_number")
    private String documentNumber;
    @Column("birth_date")
    private LocalDate birthDate;
    private String address;
    private String cellphone;
    private String email;
    private BigDecimal salary;
    @Column("role_id")
    private Long roleId;
}
