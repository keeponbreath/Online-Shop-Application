package spring.cloud.purchaseservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import spring.cloud.purchaseservice.enums.PurchaseStatus;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ownerId;
    private Long itemId;
    private Long orgId;
    private Long barCode;
    private Long storageId;
    private Double total;
    @Enumerated(EnumType.STRING)
    private PurchaseStatus status;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;
    private LocalDateTime payedAt;
}