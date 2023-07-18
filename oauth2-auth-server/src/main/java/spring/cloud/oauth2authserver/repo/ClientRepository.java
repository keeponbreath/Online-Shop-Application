package spring.cloud.oauth2authserver.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.cloud.oauth2authserver.entity.Client;

public interface ClientRepository extends JpaRepository<Client, String> {

    Optional<Client> findByClientId(String clientId);
}