package spring.cloud.storageservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "item_storage")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemStorage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "storage_id")
    @JsonBackReference(value = "storage-itemstorage")
    private Storage storageOn;
    @ManyToOne
    @JoinColumn(name = "items_id")
    @JsonBackReference(value = "iteminfo-itemstorage")
    private ItemInfo itemInfo;
    private Long count;
}
