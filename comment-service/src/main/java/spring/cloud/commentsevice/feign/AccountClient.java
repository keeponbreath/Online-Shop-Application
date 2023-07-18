package spring.cloud.commentsevice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "account-service")
public interface AccountClient {
    @RequestMapping("/accounts/check")
    ResponseEntity<Long> check();
    @RequestMapping("/accounts/{id}/name")
    String showFullName(@PathVariable("id") Long id);
}
