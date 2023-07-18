package spring.cloud.oauth2authserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.cloud.oauth2authserver.entity.Authority;
import spring.cloud.oauth2authserver.entity.SecurityUser;
import spring.cloud.oauth2authserver.repo.UserRepository;
import spring.cloud.oauth2authserver.service.AuthorityService;

import java.util.Collections;

@SpringBootApplication
@EnableDiscoveryClient
public class Oauth2AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2AuthServerApplication.class, args);
    }

//    Testing here
//    @Bean
//    CommandLineRunner run(AuthorityService service, UserRepository repository, PasswordEncoder encoder) {
//        return args -> {
//            service.save(new Authority(null, "ROLE_USER"));
//            service.save(new Authority(null, "ROLE_MODERATOR"));
//            service.save(new Authority(null, "ROLE_ADMIN"));
//            repository.save(new SecurityUser(
//                    null,
//                    "user",
//                    encoder.encode("user"),
//                    Collections.singletonList(service.findById(1L)),
//                    true,
//                    true,
//                    true,
//                    true));
//            repository.save(new SecurityUser(
//                    null,
//                    "admin",
//                    encoder.encode("admin"),
//                    service.findById(1L,3L),
//                    true,
//                    true,
//                    true,
//                    true));
//            repository.save(new SecurityUser(
//                    null,
//                    "manager",
//                    encoder.encode("manager"),
//                    service.findById(1L,2L),
//                    true,
//                    true,
//                    true,
//                    true));
//        };
//    }
}