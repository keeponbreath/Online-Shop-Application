package spring.cloud.discountservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 1, message = "Discount must be from 1 to 90 %")
    @Max(value = 90, message = "Discount must be from 1 to 90 %")
    @NotEmpty
    private Short value;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime expiration;
}