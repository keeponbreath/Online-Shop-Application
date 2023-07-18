package spring.cloud.oauth2authserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.oauth2authserver.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}