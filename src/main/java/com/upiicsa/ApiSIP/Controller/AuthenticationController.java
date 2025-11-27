package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Dto.Auth.AuthLoginDto;
import com.upiicsa.ApiSIP.Dto.Auth.AuthResponseDto;
import com.upiicsa.ApiSIP.Model.UserType;
import com.upiicsa.ApiSIP.Repository.UserRepository;
import com.upiicsa.ApiSIP.Utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@RequestBody @Valid AuthLoginDto authLogin,
                                                     HttpServletResponse response) {

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                authLogin.email(),
                authLogin.password());
        Authentication authenticatedUser = authenticationManager.authenticate(authenticationToken);
        String jwtToken = jwtUtils.createToken(authenticatedUser);

        Cookie cookie = new Cookie("jwtToken", jwtToken);

        cookie.setHttpOnly(true); //Evita que JS lea la cookie
        cookie.setSecure(true); // Solo se envía por HTTPS
        cookie.setPath("/"); // Disponible para toda la aplicación
        cookie.setMaxAge(7 * 24 * 60 * 60); // Opcional: Expira en 7 días (en segundos)

        response.addCookie(cookie);
        UserType type = userRepository.findUserTypeByEmail(authLogin.email()).orElse(null);

        assert type != null;
        return ResponseEntity.ok(new AuthResponseDto("Login OK", type.getDescription(), true));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        System.out.println("Sesion cerrada correctamente");
        return ResponseEntity.ok("Sesión cerrada correctamente");
    }
}
