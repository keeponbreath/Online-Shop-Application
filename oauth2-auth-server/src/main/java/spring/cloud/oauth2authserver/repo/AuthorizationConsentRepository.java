package spring.cloud.oauth2authserver.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.cloud.oauth2authserver.entity.AuthorizationConsent;

public interface AuthorizationConsentRepository extends JpaRepository<AuthorizationConsent, AuthorizationConsent.AuthorizationConsentId> {

    Optional<AuthorizationConsent> findByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
    void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
}