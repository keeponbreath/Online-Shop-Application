package spring.cloud.storageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.storageservice.entity.Address;

import java.util.List;

public interface AddressesRepository extends JpaRepository<Address, Long> {
    Address getAddressesByStorageId(Long id);
}
