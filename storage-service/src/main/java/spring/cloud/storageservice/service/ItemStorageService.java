package spring.cloud.storageservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.cloud.storageservice.entity.ItemInfo;
import spring.cloud.storageservice.entity.ItemStorage;
import spring.cloud.storageservice.entity.Storage;
import spring.cloud.storageservice.repository.ItemStorageRepository;

@Service
public class ItemStorageService {
    private final ItemStorageRepository repository;

    @Autowired
    public ItemStorageService(ItemStorageRepository repository) {
        this.repository = repository;
    }

    public ItemStorage getItemStorageByStorageOnAndItemInfo(Storage storage, ItemInfo itemInfo) {
        return repository.getItemStorageByStorageOnAndItemInfo(storage, itemInfo);
    }

    public ItemStorage saveItemStorage(ItemStorage itemStorage) {
        return repository.save(itemStorage);
    }

//    public void deleteItemStorage(Long id) {
//        repository.deleteById(id);
//    }
}
