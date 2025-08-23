package co.com.bancolombia.r2dbc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

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
    private String lastName;
    private Date birthDate;
    private String address;
    private String cellphone;
    @Column(unique = true)
    private String email;
    private BigDecimal salary;
}
