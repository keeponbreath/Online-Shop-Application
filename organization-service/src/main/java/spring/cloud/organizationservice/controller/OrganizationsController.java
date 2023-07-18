package spring.cloud.organizationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.cloud.organizationservice.dto.OrganizationDTO;
import spring.cloud.organizationservice.entity.Response;
import spring.cloud.organizationservice.service.OrganizationsService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/organizations")
public class OrganizationsController {
    private final OrganizationsService service;

    @Autowired
    public OrganizationsController(OrganizationsService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<Response> showOrganizations() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("organizations", service.showOrganizations()))
                        .message("Organizations loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping()
    public ResponseEntity<Response> createOrganization(@RequestBody OrganizationDTO organizationDTO) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("organization", service.createOrganization(organizationDTO)))
                        .message("Organization created")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/admin")
    public ResponseEntity<Response> getOrganizations() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("organizations", service.getOrganizations()))
                        .message("Organizations loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> showOrganizationById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("organization", service.showOrganizationById(id)))
                        .message("Organization loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/check")
    public ResponseEntity<HttpStatus> checkOwnership(@PathVariable("id") Long orgId,
                                                     @RequestParam("owner_id") Long ownerId) {
        if(service.checkOwnership(orgId, ownerId)) {
            return new ResponseEntity<>(HttpStatus.valueOf(200));
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(201));
        }
    }

    @GetMapping("/{id}/approve")
    public ResponseEntity<Response> approveOrganization(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("approved", service.approveOrganization(id)))
                        .message("Organization approved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/freeze")
    public ResponseEntity<Response> freezeOrganization(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("frozen", service.freezeOrganization(id)))
                        .message("Organization frozen")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/owner")
    public ResponseEntity<Response> getOrganizationsByOwnerId(@RequestParam("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("organizations", service.getOrganizationsByOwnerId(id)))
                        .message("Organizations loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}