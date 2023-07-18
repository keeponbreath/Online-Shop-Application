package spring.cloud.itemservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.cloud.itemservice.enums.ItemStatus;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long barCode;
    private Long orgId;
    @ManyToMany
    @JoinTable(
            name = "item_tag",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;
    private Long discountId;
    private String name;
    @Column(length = 200)
    private String description;
    private Double price;
    @Enumerated(value = EnumType.STRING)
    private ItemStatus status;
}
