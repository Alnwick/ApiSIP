package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Model.UserSIP;
import com.upiicsa.ApiSIP.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImp (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserSIP user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found."));
        return user;
    }
}
