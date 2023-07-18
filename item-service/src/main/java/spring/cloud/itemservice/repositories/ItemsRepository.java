package spring.cloud.itemservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.itemservice.entity.Item;
import spring.cloud.itemservice.entity.Tag;
import spring.cloud.itemservice.enums.ItemStatus;

import java.util.Collection;
import java.util.List;

public interface ItemsRepository extends JpaRepository<Item, Long> {
    Item getItemByBarCode(Long barCode);
    List<Item> getItemsByStatus(ItemStatus status);
    List<Item> getItemsByOrgId(Long id);
    List<Item> getItemsByStatusAndOrgId(ItemStatus status, Long id);
    List<Item> getItemsByTagsIn(Collection<List<Tag>> tags);
    List<Item> getItemsByTagsInAndStatus(Collection<List<Tag>> tags, ItemStatus status);
    List<Item> getItemsByTagsInAndOrgId(Collection<List<Tag>> tags, Long orgId);
}