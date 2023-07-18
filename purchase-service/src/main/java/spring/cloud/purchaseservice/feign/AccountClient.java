package spring.cloud.purchaseservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "account-service")
public interface AccountClient {
    @RequestMapping("/accounts/check")
    ResponseEntity<Long> check();
    @RequestMapping("/accounts/balance")
    ResponseEntity<Double> checkBalance();
}