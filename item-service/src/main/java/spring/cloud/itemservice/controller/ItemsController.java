package spring.cloud.itemservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.cloud.itemservice.dto.ItemDTO;
import spring.cloud.itemservice.entity.Response;
import spring.cloud.itemservice.service.ItemsService;
import spring.cloud.itemservice.service.TagsService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/items")
public class ItemsController {
    private final ItemsService service;
    private final TagsService tagsService;

    public ItemsController(ItemsService service, TagsService tagsService) {
        this.service = service;
        this.tagsService = tagsService;
    }

    @GetMapping()
    public ResponseEntity<Response> showAllItems() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("items", service.convert(service.showAllItems())))
                        .message("Items loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping()
    public ResponseEntity<Response> saveItem(@RequestBody ItemDTO itemDTO,
                                             Authentication auth) throws IllegalAccessException {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("item", service.convert(service.saveItem(itemDTO, auth))))
                        .message("Item saved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> showItemById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("item", service.convert(service.showItemById(id))))
                        .message("Item loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/{id}/edit")
    public ResponseEntity<Response> editItem(@PathVariable("id") Long id,
                                             @RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("item", service.convert(service.editItem(id, itemDTO))))
                        .message("Item edited")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/set_discount")
    public ResponseEntity<Response> setDiscountByItemId(@PathVariable(value = "id") Long itemId,
                                                        @RequestParam("discount_id") Long discountId) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("set", service.setDiscountByItemId(itemId, discountId)))
                        .message("Discount set")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/buy")
    public ResponseEntity<Response> buyItem(@PathVariable("id") Long itemId,
                                            @RequestParam("storage_id") Long storageId) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("bought", service.buyItem(itemId, storageId)))
                        .message("Item bought")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/approve")
    public ResponseEntity<Response> approveItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("approved", service.approveItem(id)))
                        .message("Item approved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/freeze")
    public ResponseEntity<Response> freezeItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("frozen", service.freezeItem(id)))
                        .message("Item frozen")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<Response> deleteItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("deleted", service.deleteItem(id)))
                        .message("Item deleted")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/code")
    public Long getBarCode(@PathVariable("id") Long id) {
        return service.getBarCode(id);
    }

    @GetMapping("/{id}/org")
    public ResponseEntity<Long> getOrgId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(service.getOrgId(id), HttpStatus.valueOf(200));
    }

    @GetMapping("/org")
    public ResponseEntity<Response> showItemsByOrgId(@RequestParam("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("items", service.convert(service.showItemsByOrgId(id))))
                        .message("Items loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/org/{id}/set_discount")
    public ResponseEntity<Response> setDiscountByOrgId(@PathVariable("id") Long orgId,
                                                       @RequestParam("discount_id") Long discountId,
                                                       Authentication auth) throws IllegalAccessException {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("set", service.setDiscountByOrgId(orgId, discountId, auth)))
                        .message("Discount set")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/org/{id}/tag/set_discount")
    public ResponseEntity<Response> setDiscountByTagAndOrgId(@PathVariable("id") Long orgId,
                                                             @RequestParam("name") String name,
                                                             @RequestParam("discount_id") Long discountId) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("set", service.setDiscountByTagAndOrgId(name, orgId, discountId)))
                        .message("Discount set")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/myorg")
    public ResponseEntity<Response> getItemsByOrgId(@RequestParam("id") Long id,
                                                    Authentication auth) throws IllegalAccessException {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("items", service.convert(service.getItemsByOrgId(id, auth))))
                        .message("Items loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/tag")
    public ResponseEntity<Response> showItemsByTag(@RequestParam("name") String name) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("items", service.convert(service.showItemsByTag(name))))
                        .message("Items loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/tag/set_discount")
    public ResponseEntity<Response> setDiscountByTag(@RequestParam("name") String name,
                                                     @RequestParam("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("set", service.setDiscountByTag(name, id)))
                        .message("Discount set")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/admin")
    public ResponseEntity<Response> getAllItems() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("items", service.convert(service.getAllItems())))
                        .message("Items loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<Response> getItemById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("item", service.convert(service.getItemById(id))))
                        .message("Item loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/admin/tag")
    public ResponseEntity<Response> getItemsByTag(@RequestParam("name") String name) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("items", service.convert(service.getItemsByTag(name))))
                        .message("Items loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/tags")
    public ResponseEntity<Response> showAllTags() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("tags", tagsService.getAllTags()))
                        .message("Tags loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/tags")
    public ResponseEntity<Response> deleteTag(@RequestParam("name") String name) {

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("deleted", tagsService.deleteTag(name)))
                        .message("Tags deleted")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}