package spring.cloud.accountservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@SuperBuilder
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String owner;
    private String firstName;
    private String lastName;
    @Pattern(regexp = "\\+?[7|8]\\(?\\d{3}\\)?\\d{3}-?-?\\d{2}-?\\d{2}")
    // matches:
    // +7XXXXXXXXXX
    // +7(XXX)XXXXXXX
    // +7(XXX)XXX-XX-XX
    // +7(XXX)XXX-XXXX
    // +7(XXX)XXXXX-XX
    // 7XXXXXXXXXX
    // 7(XXX)XXXXXXX
    // 7(XXX)XXX-XX-XX
    // 7(XXX)XXX-XXXX
    // 7(XXX)XXXXX-XX
    // 8XXXXXXXXXX
    // 8(XXX)XXXXXXX
    // 8(XXX)XXX-XX-XX
    // 8(XXX)XXX-XXXX
    // 8(XXX)XXXXX-XX
    private String phoneNumber;
    @Email
    private String email;
    private Boolean isActive;
    private Double balance;
    private String updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
}
