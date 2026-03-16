package com.upiicsa.ApiSIP.Service.Auth;

import com.upiicsa.ApiSIP.Dto.Email.ForgotPasswordDto;
import com.upiicsa.ApiSIP.Dto.Email.ResetPasswordDto;
import com.upiicsa.ApiSIP.Model.Token_Restore.TokenReset;
import com.upiicsa.ApiSIP.Model.UserSIP;
import com.upiicsa.ApiSIP.Repository.Token_Restore.TokenResetRepository;
import com.upiicsa.ApiSIP.Repository.UserRepository;
import com.upiicsa.ApiSIP.Service.Infrastructure.EmailService;
import com.upiicsa.ApiSIP.Service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserService userService;
    private UserRepository userRepository;
    private TokenResetRepository tokenResetRepository;
    private EmailService emailService;

    public PasswordResetService(UserRepository userRepository, TokenResetRepository tokenResetRepository,
                                EmailService emailService, UserService userService) {
        this.userRepository = userRepository;
        this.tokenResetRepository = tokenResetRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Transactional
    public void createPasswordResetToken(ForgotPasswordDto request) {
        UserSIP user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró usuario con el email: " + request.email()));

        String token = UUID.randomUUID().toString();
            TokenReset tokenReset = new TokenReset(null, token, LocalDateTime.now().plusMinutes(15), null, user);
        tokenResetRepository.save(tokenReset);

        String resetUrl = "http://localhost:8080/reset-password.html?token=" + token;

        emailService.sendResetEmail(user.getEmail(), resetUrl);

        System.out.println(">>> TOKEN DE RECUPERACIÓN GENERADO: " + token);
        System.out.println(">>> ENLACE DE VALIDACIÓN ENVIADO A: " + user.getEmail());
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

        userService.updatePassword(user, request.newPassword());

        tokenReset.setUseDate(LocalDateTime.now());
        tokenResetRepository.save(tokenReset);
    }
}
