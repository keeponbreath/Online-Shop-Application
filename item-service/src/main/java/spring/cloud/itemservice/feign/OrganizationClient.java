package spring.cloud.itemservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "organization-service")
public interface OrganizationClient {
    @RequestMapping("/organizations/{id}/check")
    ResponseEntity<HttpStatus> check(@PathVariable("id") Long orgId,
                                     @RequestParam("owner_id") Long ownerId);
}