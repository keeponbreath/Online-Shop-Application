package spring.cloud.itemservice.service;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import spring.cloud.itemservice.dto.ItemDTO;
import spring.cloud.itemservice.entity.Item;
import spring.cloud.itemservice.entity.Tag;
import spring.cloud.itemservice.enums.ItemStatus;
import spring.cloud.itemservice.exception.ItemException;
import spring.cloud.itemservice.feign.AccountClient;
import spring.cloud.itemservice.feign.DiscountClient;
import spring.cloud.itemservice.feign.OrganizationClient;
import spring.cloud.itemservice.feign.StorageClient;
import spring.cloud.itemservice.repositories.ItemsRepository;

import java.util.*;

@Service
public class ItemsService {
    private final ItemsRepository repository;
    private final TagsService tagsService;
    private final AccountClient accountClient;
    private final OrganizationClient organizationClient;
    private final DiscountClient discountClient;
    private final StorageClient storageClient;
    private final RabbitTemplate template;
    private final ModelMapper mapper;

    @Autowired
    public ItemsService(ItemsRepository repository, TagsService tagsService,
                        AccountClient accountClient, OrganizationClient organizationClient,
                        DiscountClient discountClient, StorageClient storageClient, RabbitTemplate template, ModelMapper mapper) {
        this.repository = repository;
        this.tagsService = tagsService;
        this.accountClient = accountClient;
        this.organizationClient = organizationClient;
        this.discountClient = discountClient;
        this.storageClient = storageClient;
        this.template = template;
        this.mapper = mapper;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public Item getItemById(Long id) {
        Optional<Item> itemOptional = repository.findById(id);
        return itemOptional.orElseThrow(() ->
                new NoSuchElementException("Item with id" + id + " not found"));
    }

    public Item showItemById(Long id) {
        Optional<Item> itemOptional = repository.findById(id);
        if(itemOptional.isPresent() && itemOptional.get().getStatus().equals(ItemStatus.APPROVED)) {
            return itemOptional.orElseThrow(() ->
                    new NoSuchElementException("Item with id" + id + " not found"));
        } else {
            return null;
        }
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public List<Item> getAllItems() {
        return repository.findAll();
    }

    public List<Item> showAllItems() {
        return repository.getItemsByStatus(ItemStatus.APPROVED);
    }

    public List<Item> getItemsByOrgId(Long orgId, Authentication auth) throws IllegalAccessException {
        if(accessCheck(orgId, auth)) {
            List<Item> items = repository.getItemsByOrgId(orgId);
            if(!items.isEmpty()) {
                return items;
            } else {
                throw new NoSuchElementException("No items with Org ID " + orgId + " found");
            }
        } else {
            throw new IllegalAccessException("Resource is secured");
        }
    }

    public List<Item> showItemsByOrgId(Long orgId) {
        List<Item> items = repository.getItemsByStatusAndOrgId(ItemStatus.APPROVED, orgId);
        if(!items.isEmpty()) {
            return items;
        } else {
            throw new NoSuchElementException("No items with Org ID " + orgId + " found");
        }
    }

    public List<Item> getItemsByTag(String name) {
        Tag tag = tagsService.getTagByName(name);
        return repository.getItemsByTagsIn(Collections.singleton(Collections.singletonList(tag)));
    }

    public List<Item> showItemsByTag(String name) {
        Tag tag = tagsService.getTagByName(name);
        return repository.getItemsByTagsInAndStatus(Collections.singleton(Collections.singletonList(tag)),
                ItemStatus.APPROVED);
    }

    public Item saveItem(ItemDTO itemDTO, Authentication auth) throws IllegalAccessException {
        if(accessCheck(itemDTO.getOrgId(), auth)) {
            if(getItemByBarCode(itemDTO.getBarCode()) == null) {
                Item item = convert(itemDTO);
                assert item.getId() == null;
                List<Tag> tags = new ArrayList<>();
                for (String tagName : itemDTO.getTags()) {
                    Tag tag = new Tag();
                    tag.setName(tagName.toLowerCase());
                    if (tagsService.saveTag(tag)) {
                        tags.add(tag);
                    } else {
                        tags.add(tagsService.getTagByName(tagName.toLowerCase()));
                    }
                }
                item.setTags(tags);
                item.setStatus(ItemStatus.CREATED);
                return repository.save(item);
            } else {
                throw new ItemException("Item with barcode "
                        + itemDTO.getBarCode() + " already exists");
            }
        } else {
            throw new IllegalAccessException("You can not add the item");
        }
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public Item editItem(Long id, ItemDTO itemDTO) {
        Item itemToUpdate = getItemById(id);
        itemToUpdate.setBarCode(itemDTO.getBarCode());
        List<Tag> tags = new ArrayList<>();
        for(String tag : itemDTO.getTags()) {
            if(tagsService.getTagByName(tag.toLowerCase()) != null) {
                tags.add(tagsService.getTagByName(tag.toLowerCase()));
            } else {
                Tag tagToSave = new Tag();
                tagToSave.setName(tag);
                tagsService.saveTag(tagToSave);
                tags.add(tagToSave);
            }
        }
        itemToUpdate.setTags(tags);
        itemToUpdate.setDiscountId(itemDTO.getDiscountId());
        itemToUpdate.setName(itemDTO.getName());
        itemToUpdate.setDescription(itemDTO.getDescription());
        itemToUpdate.setPrice(itemToUpdate.getPrice());
        return repository.save(itemToUpdate);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public Boolean setDiscountByItemId(Long itemId, Long discountId) {
        Item item = getItemById(itemId);
        item.setDiscountId(discountId);
        repository.save(item);
        return Boolean.TRUE;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public Boolean setDiscountByOrgId(Long orgId, Long discountId, Authentication auth) throws IllegalAccessException {
        List<Item> items = getItemsByOrgId(orgId, auth);
        for(Item item : items) {
            item.setDiscountId(discountId);
        }
        repository.saveAll(items);
        return Boolean.TRUE;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public Boolean setDiscountByTag(String tagName, Long discountId) {
        Tag tag = tagsService.getTagByName(tagName.toLowerCase());
        List<Tag> tagList = new ArrayList<>(1);
        tagList.add(tag);
        List<Item> items = repository.getItemsByTagsIn(Collections.singleton(tagList));
        for(Item item : items) {
            item.setDiscountId(discountId);
        }
        repository.saveAll(items);
        return Boolean.TRUE;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public Boolean setDiscountByTagAndOrgId(String tagName, Long orgId, Long discountId) {
        Tag tag = tagsService.getTagByName(tagName.toLowerCase());
        List<Tag> tagList = new ArrayList<>(1);
        tagList.add(tag);
        List<Item> items = repository.getItemsByTagsInAndOrgId(Collections.singleton(tagList), orgId);
        for(Item item : items) {
            item.setDiscountId(discountId);
        }
        repository.saveAll(items);
        return Boolean.TRUE;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public Boolean approveItem(Long id) {
        Optional<Item> itemOptional = repository.findById(id);
        itemOptional.ifPresent(item -> {
            item.setStatus(ItemStatus.APPROVED);
            repository.save(item);
        });
        return Boolean.TRUE;
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public Boolean freezeItem(Long id) {
        Optional<Item> itemOptional = repository.findById(id);
        itemOptional.ifPresent(item -> {
            item.setStatus(ItemStatus.CREATED);
            repository.save(item);
        });
        return Boolean.TRUE;
    }

    @RabbitListener(queues = "item_freeze")
    public void freezeItems(Long id) {
        for(Item item : repository.getItemsByOrgId(id)) {
            item.setStatus(ItemStatus.CREATED);
            repository.save(item);
        }
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public Boolean deleteItem(Long id) {
        Optional<Item> itemOptional = repository.findById(id);
        itemOptional.ifPresent(item -> {
            item.setStatus(ItemStatus.DELETED);
            repository.save(item);
        });
        return Boolean.TRUE;
    }

    public Long getBarCode(Long id) {
        return showItemById(id).getBarCode();
    }

    public Long getOrgId(Long id) {
        return showItemById(id).getOrgId();
    }

    @Transactional
    public Boolean buyItem(Long itemId, Long storageId) {
        Item itemToBuy = showItemById(itemId);
        if(check(itemId) &&
                storageClient.check(storageId, itemToBuy.getBarCode()).getStatusCode()
                        .isSameCodeAs(HttpStatusCode.valueOf(200))) {
            Long accountId = null;
            ResponseEntity<Long> accountRequest = accountClient.check();
            if(accountRequest.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200))) {
                accountId = accountRequest.getBody();
            }
            assert accountId != null;
            Short discountValue = 0;
            if(itemToBuy.getDiscountId() != null) {
                ResponseEntity<Short> discountRequest = discountClient.check(itemToBuy.getDiscountId());
                if(discountRequest.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200))) {
                    discountValue = discountRequest.getBody();
                }
            }
            assert discountValue != null;
            Double total = itemToBuy.getPrice() * (1 - ((double)discountValue / 100));
            Long barCode = itemToBuy.getBarCode();
            Long orgId = itemToBuy.getOrgId();
            Map<String, String> info = new HashMap<>();
            info.put("accountId", accountId.toString());
            info.put("itemId", itemId.toString());
            info.put("orgId", orgId.toString());
            info.put("barCode", barCode.toString());
            info.put("storageId", storageId.toString());
            info.put("storage_action", "outcome");
            info.put("total", total.toString());
            template.convertAndSend("purchase_action", "purchase_create", info);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Item getItemByBarCode(Long barCode) {
        return repository.getItemByBarCode(barCode);
    }

    public boolean check(Long id) {
        Optional<Item> itemOptional = repository.findById(id);
        return itemOptional.map(item -> item.getStatus().equals(ItemStatus.APPROVED)).orElse(false);
    }

    private boolean accessCheck(Long orgId, Authentication auth) {
        if(auth.getAuthorities().toString().contains("ROLE_ADMIN")) {
            return true;
        } else {
            ResponseEntity<Long> accountRequest = accountClient.check();
            Long ownerId = accountRequest.getBody();
            return accountRequest.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200))
                    && organizationClient.check(orgId, ownerId).getStatusCode()
                    .isSameCodeAs(HttpStatusCode.valueOf(200));
        }
    }

    private Item convert(ItemDTO itemDTO) {
        mapper.getConfiguration().setAmbiguityIgnored(true);
        return mapper.map(itemDTO, Item.class);
    }

    public ItemDTO convert(Item item) {
        mapper.getConfiguration().setAmbiguityIgnored(true);
        ItemDTO itemDTO = mapper.map(item, ItemDTO.class);
        itemDTO.setTags(convertTags(item.getTags()));
        return itemDTO;
    }

    public List<ItemDTO> convert(List<Item> items) {
        List<ItemDTO> itemDTOS = new ArrayList<>();
        for(Item item : items) {
            ItemDTO itemDTO = convert(item);
            itemDTOS.add(itemDTO);
        }
        return itemDTOS;
    }

    public List<String> convertTags(List<Tag> tags) {
        List<String> stringTags = new ArrayList<>();
        for(Tag tag : tags) {
            stringTags.add(tag.getName().toLowerCase());
        }
        return stringTags;
    }
}