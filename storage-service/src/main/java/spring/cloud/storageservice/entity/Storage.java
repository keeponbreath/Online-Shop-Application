package spring.cloud.storageservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.cloud.storageservice.enums.StorageStatus;

import java.util.List;

@Entity
@Table(name = "storages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storage_id")
    private Long id;
    private String name;
    @OneToOne(mappedBy = "storage")
    @JsonBackReference(value = "storage-address")
    private Address address;
    @Enumerated(EnumType.STRING)
    private StorageStatus status;
    @OneToMany(mappedBy = "storageOn", cascade = CascadeType.ALL)
    @JsonBackReference(value = "storage-itemstorage")
    private List<ItemStorage> itemStorages;
}
