package spring.cloud.storageservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.cloud.storageservice.entity.ItemInfo;
import spring.cloud.storageservice.repository.ItemInfoRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ItemInfoService {
    private final ItemInfoRepository repository;

    @Autowired
    public ItemInfoService(ItemInfoRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void saveItemInfo(ItemInfo itemInfo) {
        repository.save(itemInfo);
    }

    public ItemInfo getItemInfoByBarCode(Long barCode) {
        Optional<ItemInfo> itemInfoOptional = Optional.ofNullable(repository.getItemInfoByBarCode(barCode));
        return itemInfoOptional.orElseThrow(() ->
                new NoSuchElementException("No item with barcode " + barCode + " found"));
    }

    public ItemInfo getItemInfoByBarCodeForced(Long barCode) {
        Optional<ItemInfo> itemInfoOptional = Optional.ofNullable(repository.getItemInfoByBarCode(barCode));
        return itemInfoOptional.orElse(null);
    }
}