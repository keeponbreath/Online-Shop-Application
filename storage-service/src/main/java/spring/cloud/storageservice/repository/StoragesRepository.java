package spring.cloud.storageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.storageservice.entity.ItemStorage;
import spring.cloud.storageservice.entity.Storage;

import java.util.List;

public interface StoragesRepository extends JpaRepository<Storage, Long> {
    List<Storage> getStoragesByAddress_City(String city);
}
