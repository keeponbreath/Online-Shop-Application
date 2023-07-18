package spring.cloud.storageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.storageservice.entity.ItemInfo;
import spring.cloud.storageservice.entity.ItemStorage;
import spring.cloud.storageservice.entity.Storage;

public interface ItemStorageRepository extends JpaRepository<ItemStorage, Long> {
    ItemStorage getItemStorageByStorageOnAndItemInfo(Storage storage, ItemInfo itemInfo);
}
