package spring.cloud.itemservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.cloud.itemservice.entity.Tag;
import spring.cloud.itemservice.repositories.TagsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TagsService {
    private final TagsRepository repository;

    @Autowired
    public TagsService(TagsRepository repository) {
        this.repository = repository;
    }

    public List<String> getAllTags() {
        List<String> tags = new ArrayList<>();
        for(Tag tag : repository.findAll()) {
            tags.add(tag.getName());
        }
        return tags;
    }

    public Tag getTagByName(String name) {
        Optional<Tag> tagOptional = Optional.ofNullable(repository.getTagByName(name.toLowerCase()));
        return tagOptional.orElseThrow(() ->
                new NoSuchElementException("No tag " + name + " found"));
    }

    public Boolean saveTag(Tag tag) {
        Optional<Tag> tagOptional = Optional.ofNullable(repository.getTagByName(tag.getName()));
        if(tagOptional.isEmpty()) {
            tag.setName(tag.getName().toLowerCase());
            repository.save(tag);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public Boolean deleteTag(String name) {
        Optional<Tag> tagOptional = Optional.ofNullable(repository.getTagByName(name.toLowerCase()));
        if(tagOptional.isPresent()) {
            repository.deleteTagByName(name);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}