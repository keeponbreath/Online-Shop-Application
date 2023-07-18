package spring.cloud.oauth2authserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.oauth2authserver.entity.SecurityUser;

public interface UserRepository extends JpaRepository<SecurityUser, Long> {
    SecurityUser findByUsername(String username);
}