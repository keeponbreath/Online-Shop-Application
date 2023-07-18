package spring.cloud.oauth2authserver.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.cloud.oauth2authserver.entity.SecurityUser;
import spring.cloud.oauth2authserver.repo.UserRepository;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class SecurityUserService {

    private final UserRepository userRepository;
    private final AuthorityService authorityService;
    private final PasswordEncoder encoder;
    private final RabbitTemplate template;

    @Autowired
    public SecurityUserService(UserRepository userRepository, AuthorityService authorityService,
                               PasswordEncoder encoder, RabbitTemplate template) {
        this.userRepository = userRepository;
        this.authorityService = authorityService;
        this.encoder = encoder;
        this.template = template;
    }

    private boolean check(String name) {
        Optional<SecurityUser> user = Optional.ofNullable(userRepository.findByUsername(name));
        return user.isPresent();
    }

    public SecurityUser save(SecurityUser user) {
        if(check(user.getUsername())) {
            throw new RuntimeException("User already exists!");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAuthorities(Collections.singletonList(authorityService.findById(1L)));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        template.convertAndSend("user_create", "user_create",
                Map.of("username", user.getUsername()));
        return userRepository.save(user);
    }
}