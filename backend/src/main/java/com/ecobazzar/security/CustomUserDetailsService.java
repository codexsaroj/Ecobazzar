package com.ecobazzar.security;

import com.ecobazzar.exception.NotFoundException;
import com.ecobazzar.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User/ Email Not found"));
    }
}
