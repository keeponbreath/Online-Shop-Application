package spring.cloud.oauth2authserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class SecurityUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String username;

    @NonNull
    private String password;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "users_authorities", joinColumns = {
            @JoinColumn(name = "USERS_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
            @JoinColumn(name = "AUTHORITIES_ID", referencedColumnName = "ID") })
    private List<Authority> authorities;

    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
}
