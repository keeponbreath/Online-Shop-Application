package spring.cloud.discountservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.cloud.discountservice.dto.DiscountDTO;
import spring.cloud.discountservice.entity.Response;
import spring.cloud.discountservice.service.DiscountsService;
import spring.cloud.discountservice.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/discounts")
public class DiscountsController {
    private final DiscountsService service;

    @Autowired
    public DiscountsController(DiscountsService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<Response> getAllDiscounts() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("discounts", service.getAllDiscounts()))
                        .message("Discounts loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping()
    public ResponseEntity<Response> createDiscount(@RequestBody @Valid DiscountDTO request,
                                 BindingResult result) {
        ValidationUtil.checkResult(result);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("discount",
                                service.createDiscount(request.getExpiration(), request.getValue())))
                        .message("Discount saved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getDiscountById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("discount", service.getDiscountById(id)))
                        .message("Discount loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> editDiscount(@PathVariable("id") Long id,
                               @RequestBody @Valid DiscountDTO request,
                               BindingResult result) {
        ValidationUtil.checkResult(result);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("updated",
                                service.editDiscount(id, request.getExpiration(), request.getValue())))
                        .message("Discount updated")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteDiscount(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("deleted", service.deleteDiscount(id)))
                        .message("Discount deleted")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/check")
    public ResponseEntity<Short> checkInfo(@PathVariable("id") Long id) {
        if(service.checkInfo(id)) {
            return new ResponseEntity<>(service.getDiscountById(id).getValue(),
                    HttpStatus.valueOf(200));
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(201));
        }
    }
}