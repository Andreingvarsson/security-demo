package com.example.demo.services;
import com.example.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Username" + username + " not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), getGrantedAuthorities(user));
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(User user){
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("Role_" + role)) // ROLE_ADMIN -> ADMIN
                .collect(Collectors.toList());
    }

}
