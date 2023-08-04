package my.restaurant.service.impl;

import jakarta.transaction.Transactional;
import my.restaurant.dto.MyUserDetails;
import my.restaurant.entity.Role;
import my.restaurant.entity.User;
import my.restaurant.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) throw new UsernameNotFoundException(username);
        User user = userOptional.get();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName().toString()));
        }
        MyUserDetails myUserDetails = new MyUserDetails(user.getUsername(), user.getPassword(), true, true, true, true, grantedAuthorities);
        myUserDetails.setImgUrl(user.getImgUrl());
        myUserDetails.setFirstName(user.getFirstName());
        myUserDetails.setLastName(user.getLastName());
        return myUserDetails;
    }
}
