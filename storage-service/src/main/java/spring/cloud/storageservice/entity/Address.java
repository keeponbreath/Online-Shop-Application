package spring.cloud.storageservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String city;
    private String street;
    private String house;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "_storage_id", referencedColumnName = "storage_id")
    @JsonBackReference(value = "storage-address")
    private Storage storage;
}