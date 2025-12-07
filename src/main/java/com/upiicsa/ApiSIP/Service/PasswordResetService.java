package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.Email.ForgotPasswordDto;
import com.upiicsa.ApiSIP.Dto.Email.ResetPasswordDto;
import com.upiicsa.ApiSIP.Model.Token_Restore.TokenReset;
import com.upiicsa.ApiSIP.Model.UserSIP;
import com.upiicsa.ApiSIP.Repository.Token_Restore.TokenResetRepository;
import com.upiicsa.ApiSIP.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenResetRepository tokenResetRepository;

    @Autowired
    private PasswordEncoder  passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void createPasswordResetToken(ForgotPasswordDto request) {
        UserSIP user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró usuario con el email: " + request.email()));

        String token = UUID.randomUUID().toString();
            TokenReset tokenReset = new TokenReset(null, token, LocalDateTime.now(), null, user);
        tokenResetRepository.save(tokenReset);

        String resetUrl = "http://localhost:8080/api/reset-password/validate?token=" + token;

        emailService.sendResetEmail(user.getEmail(), resetUrl);

        System.out.println("\n------------------------------------------------------");
        System.out.println(">>> TOKEN DE RECUPERACIÓN GENERADO: " + token);
        System.out.println(">>> ENLACE DE VALIDACIÓN ENVIADO A: " + user.getEmail());
        System.out.println(">>> URL DE VALIDACIÓN (GET): " + resetUrl);
        System.out.println("------------------------------------------------------\n");
    }

    @Transactional(readOnly = true)
    public boolean validatePasswordResetToken(String token) {
        Optional<TokenReset> tokenReset = tokenResetRepository.findByToken(token);

        if (tokenReset.isEmpty()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        return !tokenReset.get().getExpirationDate().isBefore(now);
    }

    @Transactional
    public void resetPassword(ResetPasswordDto request) {
        TokenReset tokenReset = tokenResetRepository.findByToken(request.token())
                .orElseThrow(() -> new IllegalArgumentException("Token inválido o ya utilizado."));

        if (tokenReset.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Expired token. Please Request a new.");
        }

        UserSIP user = tokenReset.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        tokenReset.setUseDate(LocalDateTime.now());
    }
}
