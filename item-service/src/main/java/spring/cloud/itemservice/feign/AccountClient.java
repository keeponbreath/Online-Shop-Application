package spring.cloud.itemservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@FeignClient(value = "account-service")
public interface AccountClient {
    @RequestMapping("/accounts/check")
    ResponseEntity<Long> check();
}