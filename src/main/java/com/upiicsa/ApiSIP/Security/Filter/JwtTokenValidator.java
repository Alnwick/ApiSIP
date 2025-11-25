package com.upiicsa.ApiSIP.Security.Filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.upiicsa.ApiSIP.Model.Usuario;
import com.upiicsa.ApiSIP.Repository.UsuarioRepository;
import com.upiicsa.ApiSIP.Utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtTokenValidator extends OncePerRequestFilter {

    private UsuarioRepository repository;
    private JwtUtils jwtUtils;

    public JwtTokenValidator(JwtUtils jwtUtils, UsuarioRepository repository) {
        this.jwtUtils = jwtUtils;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = null;

        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtToken != null) {
            try {
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);
                String email = jwtUtils.extractUsername(decodedJWT);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Cargar el usuario de la base de datos
                    Usuario usuario = repository.findByCorreo(email).orElse(null);

                    if (usuario != null) {
                        //Crear autenticacion para el usuario
                        Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null,
                                usuario.getAuthorities());
                        //Colocar la autenticacion en el contexto de seguridad
                        SecurityContext context = SecurityContextHolder.getContext();
                        context.setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error to validate token" + e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
