package spring.cloud.purchaseservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.cloud.purchaseservice.entity.Response;
import spring.cloud.purchaseservice.service.PurchaseService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {
    private final PurchaseService service;

    @Autowired
    public PurchaseController(PurchaseService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<Response> getAllPurchases() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("purchases", service.getAllPurchases()))
                        .message("Purchases loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPurchaseById(@PathVariable("id") Long id,
                                                    Authentication auth) throws IllegalAccessException {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("purchase", service.getPurchaseById(id, auth)))
                        .message("Purchase loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/pay")
    public ResponseEntity<Response> payForPurchase(@PathVariable("id") Long id,
                                                   Authentication auth) throws IllegalAccessException {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("paid", service.payForPurchase(id, auth)))
                        .message("Purchase has been paid")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/return")
    public ResponseEntity<Response> returnPurchase(@PathVariable("id") Long id,
                                                   Authentication auth) throws IllegalAccessException {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("returned", service.returnPurchase(id, auth)))
                        .message("Purchase has been returned")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/owner")
    public ResponseEntity<Response> getPurchasesByOwnerId(@RequestParam("id") Long ownerId,
                                                          @RequestParam(value = "item_id", required = false) Long itemId,
                                                          Authentication auth) throws IllegalAccessException {
        if(itemId == null) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("purchases", service
                                    .getPurchasesByOwnerId(ownerId, auth)))
                            .message("Purchases loaded")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("purchases", service
                                    .getPurchasesByItemIdAndOwnerId(itemId, ownerId, auth)))
                            .message("Purchases loaded")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }

    }

    @GetMapping("/item")
    public ResponseEntity<Response> getPurchasesByItemId(@RequestParam("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("purchases", service.getPurchasesByItemId(id)))
                        .message("Purchases loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/comment_check")
    public ResponseEntity<HttpStatus> commentCheck(@RequestParam("item_id") Long itemId,
                                                   @RequestParam("owner_id") Long ownerId) {
        if(service.commentCheck(itemId, ownerId)) {
            return new ResponseEntity<>(HttpStatus.valueOf(200));
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(201));
        }
    }
}