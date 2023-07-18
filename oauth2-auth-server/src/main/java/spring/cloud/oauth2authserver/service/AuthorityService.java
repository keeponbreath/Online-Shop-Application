package spring.cloud.oauth2authserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.cloud.oauth2authserver.entity.Authority;
import spring.cloud.oauth2authserver.repo.AuthorityRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityService {
    private final AuthorityRepository repository;

    @Autowired
    public AuthorityService(AuthorityRepository repository) {
        this.repository = repository;
    }

    public void save(Authority authority) {
        repository.save(authority);
    }

    public Authority findById(Long id) {
        return repository.findById(id).orElseThrow(() ->
            new RuntimeException("Authority with: " + id + "not found!"));
    }

    public List<Authority> findById(Long... id) {
        List<Authority> authorities = new ArrayList<>();
        for(Long a : id) {
            authorities.add(repository.findById(a).orElse(null));
        }
        return authorities;
    }
}