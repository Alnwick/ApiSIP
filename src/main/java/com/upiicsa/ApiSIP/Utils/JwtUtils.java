package com.upiicsa.ApiSIP.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.upiicsa.ApiSIP.Model.UserType;
import com.upiicsa.ApiSIP.Model.UserSIP;
import com.upiicsa.ApiSIP.Repository.Token_Restore.DurationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private final DurationTokenRepository durationTokenRepository;

    @Value("${api.security.jwt.private.key}")
    private String privateKey;

    @Value("${api.security.jwt.user.generator}")
    private String userGenerator;

    public JwtUtils(DurationTokenRepository durationTokenRepository) {
        this.durationTokenRepository = durationTokenRepository;
    }

    //Metodo para crear el token del usuario
    public String createToken(Authentication authentication) {
        try {
            UserSIP user = (UserSIP) authentication.getPrincipal();
            Algorithm algorithm = Algorithm.HMAC256(privateKey);

            // Obtener los permisos del usuario autenticado
            String authorities = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            // Crear el token para el usuario
            return JWT.create()
                    .withIssuer(userGenerator)
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withClaim("authorities", authorities)
                    .withExpiresAt(generateDateExpiration(user.getUserType()))
                    .sign(algorithm);
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error to create JWT Token", exception);
        }
    }

    //Metodo para validar el token
    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(userGenerator)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token invalid o r expired. Not authorized.");
        }
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    //Metodo para genera la fecha de expiracion del token
    private Instant generateDateExpiration(UserType userType) {
        return LocalDateTime.now().plusHours(durationTokenRepository.getHoursByUserType(userType))
                .toInstant(ZoneOffset.of("-05:00"));
    }
}
