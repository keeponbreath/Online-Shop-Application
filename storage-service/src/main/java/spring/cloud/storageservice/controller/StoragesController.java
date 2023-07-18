package spring.cloud.storageservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.cloud.storageservice.entity.Address;
import spring.cloud.storageservice.entity.ItemInfo;
import spring.cloud.storageservice.entity.Response;
import spring.cloud.storageservice.entity.Storage;
import spring.cloud.storageservice.service.AddressesService;
import spring.cloud.storageservice.service.StoragesService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/storages")
public class StoragesController {
    private final StoragesService storagesService;
    private final AddressesService addressesService;

    @Autowired
    public StoragesController(StoragesService storagesService,
                              AddressesService addressesService) {
        this.storagesService = storagesService;
        this.addressesService = addressesService;
    }

    @GetMapping()
    public ResponseEntity<Response> getAllStorages() {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("storages", storagesService.convert(storagesService.getAllStorages())))
                        .message("Storages loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping()
    public ResponseEntity<Response> createStorage(@RequestBody Storage storage) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("storage", storagesService.createStorage(storage)))
                        .message("Storage created")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/item")
    public ResponseEntity<Response> getStoragesByBarCode(@RequestParam("barcode") Long barCode) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("storages", storagesService
                                .convert(storagesService.getStoragesByBarCode(barCode))))
                        .message("Storages loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getStorageById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("storage", storagesService
                                .convert(storagesService.getStorageById(id))))
                        .message("Storage loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{storage_id}/item")
    public ResponseEntity<Response> getItemStorage(@PathVariable("storage_id") Long storageId,
                                                   @RequestParam("barcode") Long barCode) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("item", storagesService
                                .convert(storagesService.getItemStorage(storageId, barCode))))
                        .message("Item loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<Response> getAllItemsInStorage(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("items", storagesService.getAllItemsInStorage(id)))
                        .message("Items loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/{id}/put")
    public ResponseEntity<Response> putItem(@PathVariable("id") Long id,
                                            @RequestBody ItemInfo itemInfo,
                                            @RequestParam("count") Long count) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("Item", storagesService.putItem(id, itemInfo, count)))
                        .message("Item has been put")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

//    @PatchMapping("/{id}/take")
//    public String takeItem(@PathVariable("id") Long storageId,
//                           @RequestParam("barcode") Long barCode) {
//        storagesService.takeItem(storageId, barCode);
//        return "Item taken";
//    }

    @PatchMapping("/{id}/edit")
    public ResponseEntity<Response> editStoragesAddress(@PathVariable("id") Long id,
                                                        @RequestBody Address address) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("updated", addressesService.editAddress(id, address)))
                        .message("Address has been updated")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/open")
    public ResponseEntity<Response> openStorage(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("opened", storagesService.openStorage(id)))
                        .message("Storage has been opened")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/{id}/check")
    public ResponseEntity<HttpStatus> check(@PathVariable("id") Long id,
                                            @RequestParam("barcode") Long barCode) {
        if(storagesService.check(id, barCode)) {
            return new ResponseEntity<>(HttpStatus.valueOf(200));
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(201));
        }
    }

    @GetMapping("/find")
    public ResponseEntity<Response> getStoragesByAddress_City(@RequestParam("city") String city) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("storages", storagesService.getStoragesByAddress_City(city)))
                        .message("Storages loaded")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}