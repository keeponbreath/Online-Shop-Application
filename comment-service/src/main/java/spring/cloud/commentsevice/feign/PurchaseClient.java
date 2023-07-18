package spring.cloud.commentsevice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("purchase-service")
public interface PurchaseClient {
    @RequestMapping("/purchases/comment_check")
    ResponseEntity<HttpStatus> purchaseCheck(@RequestParam("item_id") Long itemId,
                                             @RequestParam("owner_id") Long ownerId);
}
