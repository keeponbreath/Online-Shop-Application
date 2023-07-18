package spring.cloud.itemservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
    private List<Item> items;
    @Column(unique = true)
    private String name;

    @Override
    public String toString() {
        return name;
    }
}