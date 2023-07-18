package spring.cloud.organizationservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.cloud.organizationservice.dto.OrganizationDTO;
import spring.cloud.organizationservice.repository.OrganizationsRepository;
import spring.cloud.organizationservice.feign.AccountClient;
import spring.cloud.organizationservice.entity.Organization;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrganizationsService {
    private final OrganizationsRepository repository;
    private final AccountClient client;
    private final RabbitTemplate template;

    @Autowired
    public OrganizationsService(OrganizationsRepository repository, AccountClient client,
                                RabbitTemplate template) {
        this.repository = repository;
        this.client = client;
        this.template = template;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public List<Organization> getOrganizations() {
        return repository.findAll();
    }

    public List<Organization> showOrganizations() {
        return repository.findAll().stream().filter(Organization::getActive).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public Organization getOrganizationById(Long id) {
        Optional<Organization> organizationOptional = repository.findById(id);
        return organizationOptional.orElseThrow(() ->
                new NoSuchElementException("Organization with " + id + " not found"));
    }

    public Organization showOrganizationById(Long id) {
        Optional<Organization> organizationOptional = repository.findById(id);
        if(organizationOptional.isPresent() && organizationOptional.get().getActive()) {
            return organizationOptional.orElseThrow(() ->
                    new NoSuchElementException("Organization with " + id + " not found"));
        } else {
            throw new NoSuchElementException("Organization with " + id + " not found");
        }
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public List<Organization> getOrganizationsByOwnerId(Long id) {
        List<Organization> organizations = repository.getOrganizationsByOwnerId(id);
        if(!organizations.isEmpty()) {
            return organizations;
        } else {
            throw new NoSuchElementException("No organizations with Owner ID " + id + " found");
        }
    }

    public Organization createOrganization(OrganizationDTO organizationDTO) {
        ResponseEntity<Long> request = client.check();
        if(request.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200))) {
            Organization organization = new Organization();
            organization.setName(organizationDTO.getName());
            organization.setDescription(organizationDTO.getDescription());
            organization.setOwnerId(request.getBody());
            organization.setActive(false);
            organization.setBalance(0.0);
            return repository.save(organization);
        } else {
            throw new RuntimeException("Your account is not active or remote resource is inaccessible");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean approveOrganization(Long id) {
        Organization organization = getOrganizationById(id);
        organization.setActive(true);
        repository.save(organization);
        return Boolean.TRUE;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean freezeOrganization(Long id) {
        Organization organization = getOrganizationById(id);
        organization.setActive(false);
        repository.save(organization);
        template.convertAndSend("org_freeze", "org_freeze", id);
        return Boolean.TRUE;
    }

    @RabbitListener(queues = "org_payment")
    public void changeBalance(Map<String, String> info) {
        Long orgId = Long.valueOf(info.get("orgId"));
        Organization organization = repository.findById(orgId).orElse(null);
        if(organization != null) {
            Double value = Double.valueOf(info.get("value"));
            String action = info.get("org_action");
            if (action.equals("income")) {
                organization.setBalance(organization.getBalance() + value);
            } else {
                organization.setBalance(organization.getBalance() - value);
            }
            repository.save(organization);
        }
    }

    public boolean checkOwnership(Long orgId, Long ownerId) {
        return getOrganizationById(orgId).getOwnerId().equals(ownerId);
    }
}