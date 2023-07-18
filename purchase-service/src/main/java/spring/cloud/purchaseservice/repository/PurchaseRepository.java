package spring.cloud.purchaseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.purchaseservice.entity.Purchase;
import spring.cloud.purchaseservice.enums.PurchaseStatus;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> getPurchasesByItemId(Long id);
    List<Purchase> getPurchasesByOwnerId(Long id);
    List<Purchase> getPurchasesByItemIdAndOwnerId(Long itemId, Long ownerId);
    List<Purchase> getPurchasesByStatus(PurchaseStatus status);
    List<Purchase> getPurchasesByItemIdAndOwnerIdAndStatus(Long itemId, Long ownerId, PurchaseStatus status);
}
