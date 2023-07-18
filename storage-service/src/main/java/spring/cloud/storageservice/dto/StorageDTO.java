package spring.cloud.storageservice.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.cloud.storageservice.entity.Address;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageDTO {
    private Long id;
    private String name;
    private Address address;
}
