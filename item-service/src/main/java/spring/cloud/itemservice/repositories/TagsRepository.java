package spring.cloud.itemservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.cloud.itemservice.entity.Tag;

public interface TagsRepository extends JpaRepository<Tag, Long> {
    void deleteTagByName(String name);
    Tag getTagByName(String name);
}
