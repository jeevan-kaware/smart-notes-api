package com.jeevan.smart_notes_api.service;

import com.jeevan.smart_notes_api.entity.User;
import com.jeevan.smart_notes_api.repository.UserRepository;
import com.jeevan.smart_notes_api.security.UserPrincipl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return new UserPrincipl(user);
    }
}