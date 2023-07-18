package spring.cloud.organizationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.organizationservice.entity.Organization;

import java.util.List;

public interface OrganizationsRepository extends JpaRepository<Organization, Long> {
    List<Organization> getOrganizationsByOwnerId(Long id);
}
