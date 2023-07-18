package spring.cloud.storageservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "items_id")
    @JsonIgnore
    private Long id;
    private Long barCode;
    @OneToMany(mappedBy = "itemInfo", cascade = CascadeType.ALL)
    @JsonBackReference(value = "iteminfo-itemstorage")
    private List<ItemStorage> itemStorages;
}
