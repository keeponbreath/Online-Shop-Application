package spring.cloud.oauth2authserver.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import spring.cloud.oauth2authserver.entity.SecurityUser;
import spring.cloud.oauth2authserver.repo.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class JpaUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;

    @Override
    public void createUser(UserDetails user) {
        //todo some logic here
    }

    @Override
    public void updateUser(UserDetails user) {
        //todo some logic here
        // template.convertAndSend("user_create", "user_create",
        //                Map.of("username", user.getUsername()));
    }

    @Override
    public void deleteUser(String username) {
        //todo some logic here
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        //todo some logic here
    }

    @Override
    public boolean userExists(String username) {
        SecurityUser user = userRepository.findByUsername(username);
        return user.getUsername().equals(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser user = userRepository.findByUsername(username);
        if(!user.getUsername().equals(username)) {
            throw new UsernameNotFoundException("Access denied");
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        user.getAuthorities().forEach(auth -> authorities.add(new SimpleGrantedAuthority(auth.getAuthority())));
        return new User(user.getUsername(), user.getPassword(), user.getEnabled(), user.getAccountNonExpired(),
                user.getCredentialsNonExpired(), user.getAccountNonLocked(), authorities);
    }
}