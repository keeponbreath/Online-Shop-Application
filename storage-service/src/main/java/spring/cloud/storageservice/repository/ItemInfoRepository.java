package spring.cloud.storageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.storageservice.entity.ItemInfo;

public interface ItemInfoRepository extends JpaRepository<ItemInfo, Long> {
    ItemInfo getItemInfoByBarCode(Long barCode);
}
