package spring.cloud.itemservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "discount-service")
public interface DiscountClient {
    @RequestMapping("/discounts/{id}/check")
    ResponseEntity<Short> check(@PathVariable("id") Long id);
}
