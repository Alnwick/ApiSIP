package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Model.Usuario;
import com.upiicsa.ApiSIP.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repository.findByCorreo(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found."));
        return usuario;
    }
}
