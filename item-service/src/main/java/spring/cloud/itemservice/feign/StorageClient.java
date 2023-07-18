package spring.cloud.itemservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "storage-service")
public interface StorageClient {
    @RequestMapping("/storages/{id}/check")
    ResponseEntity<HttpStatus> check(@PathVariable("id") Long storageId,
                                     @RequestParam("barcode") Long barCode);
}
