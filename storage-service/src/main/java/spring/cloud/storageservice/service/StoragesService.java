package spring.cloud.storageservice.service;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.cloud.storageservice.dto.ItemInStorageDTO;
import spring.cloud.storageservice.dto.StorageDTO;
import spring.cloud.storageservice.entity.ItemInfo;
import spring.cloud.storageservice.entity.ItemStorage;
import spring.cloud.storageservice.entity.Storage;
import spring.cloud.storageservice.enums.StorageStatus;
import spring.cloud.storageservice.repository.StoragesRepository;

import java.util.*;

@Service
public class StoragesService {
    private final StoragesRepository repository;
    private final AddressesService addressesService;
    private final ItemInfoService itemInfoService;
    private final ItemStorageService itemStorageService;
    private final ModelMapper mapper;

    @Autowired
    public StoragesService(StoragesRepository repository,
                           AddressesService addressesService,
                           ItemInfoService itemInfoService,
                           ItemStorageService itemStorageService,
                           ModelMapper mapper) {
        this.repository = repository;
        this.addressesService = addressesService;
        this.itemInfoService = itemInfoService;
        this.itemStorageService = itemStorageService;
        this.mapper = mapper;
    }

    public List<Storage> getAllStorages() {
        return repository.findAll();
    }

    public Storage getStorageById(Long id) {
        Optional<Storage> storageOptional = repository.findById(id);
        return storageOptional.orElseThrow(() ->
                new NoSuchElementException("Storage with id" + id + " not found"));
    }

    public List<Storage> getStoragesByAddress_City(String city) {
        List<Storage> storages = repository.getStoragesByAddress_City(city);
        if(!storages.isEmpty()) {
            return storages;
        } else {
            throw new NoSuchElementException("No storages in " + city + " found");
        }
    }

    public List<Storage> getStoragesByBarCode(Long barCode) {
        ItemInfo itemInfo = itemInfoService.getItemInfoByBarCode(barCode);
        List<Storage> storages = new ArrayList<>();
        for(ItemStorage itemStorage : itemInfo.getItemStorages()) {
            storages.add(itemStorage.getStorageOn());
        }
        return storages;
    }

    public List<ItemInStorageDTO> getAllItemsInStorage(Long id) {
        List<ItemStorage> itemStorages = getStorageById(id).getItemStorages();
        List<ItemInStorageDTO> itemInStorageDTOS = new ArrayList<>(itemStorages.size());
        for(ItemStorage itemStorage : itemStorages) {
            itemInStorageDTOS.add(new ItemInStorageDTO(itemStorage.getItemInfo(), itemStorage.getCount()));
        }
        return itemInStorageDTOS;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Storage createStorage(Storage storage) {
        storage.setStatus(StorageStatus.CLOSED);
        storage.getAddress().setStorage(storage);
        addressesService.saveAddress(storage.getAddress());
        return repository.save(storage);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean openStorage(Long id) {
        Storage storage = getStorageById(id);
        storage.setStatus(StorageStatus.OPENED);
        repository.save(storage);
        return Boolean.TRUE;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ItemStorage putItem(Long id, ItemInfo itemInfo, Long count) {
        if(itemInfoService.getItemInfoByBarCodeForced(itemInfo.getBarCode()) == null) {
            itemInfoService.saveItemInfo(itemInfo);
        }
        ItemStorage existingItemStorage = itemStorageService
                .getItemStorageByStorageOnAndItemInfo(getStorageById(id), itemInfo);
        if(existingItemStorage == null) {
            ItemStorage itemStorage = new ItemStorage();
            itemStorage.setStorageOn(getStorageById(id));
            itemStorage.setItemInfo(itemInfoService.getItemInfoByBarCode(itemInfo.getBarCode()));
            itemStorage.setCount(count);
            return itemStorageService.saveItemStorage(itemStorage);
        } else {
            existingItemStorage.setCount(existingItemStorage.getCount() + count);
            return itemStorageService.saveItemStorage(existingItemStorage);
        }
    }

    @RabbitListener(queues = "storage_count")
    public void changeCount(Map<String, String> info) {
        Long storageId = Long.valueOf(info.get("storageId"));
        Long barCode = Long.valueOf(info.get("barCode"));
        ItemStorage itemStorage = getItemStorage(storageId, barCode);
        if(itemStorage.getCount() != 0) {
            String action = info.get("storage_action");
            if(action.equals("outcome")) {
                itemStorage.setCount(itemStorage.getCount() - 1);
                itemStorageService.saveItemStorage(itemStorage);
            } else {
                itemStorage.setCount(itemStorage.getCount() + 1);
                itemStorageService.saveItemStorage(itemStorage);
            }
        }
    }

    public ItemStorage getItemStorage(Long storageId, Long barCode) {
        Storage storage = getStorageById(storageId);
        ItemInfo itemInfo = itemInfoService.getItemInfoByBarCode(barCode);
        return itemStorageService.getItemStorageByStorageOnAndItemInfo(storage, itemInfo);
    }

    public boolean check(Long storageId, Long barCode) {
        Optional<Storage> storageOptional = repository.findById(storageId);
        ItemStorage itemStorage = getItemStorage(storageId, barCode);
        return storageOptional.isPresent() && itemStorage.getCount() != 0;
    }

    public ItemInStorageDTO convert(ItemStorage itemStorage) {
        return new ItemInStorageDTO(itemStorage.getItemInfo(), itemStorage.getCount());
    }

    public StorageDTO convert(Storage storage) {
        return mapper.map(storage, StorageDTO.class);
    }

    public List<StorageDTO> convert(List<Storage> storages) {
        List<StorageDTO> storageDTOS = new ArrayList<>();
        for(Storage storage : storages) {
            storageDTOS.add(convert(storage));
        }
        return storageDTOS;
    }
}