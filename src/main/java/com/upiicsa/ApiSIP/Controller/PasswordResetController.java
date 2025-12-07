package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Dto.Email.ForgotPasswordDto;
import com.upiicsa.ApiSIP.Dto.Email.ResetPasswordDto;
import com.upiicsa.ApiSIP.Service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PasswordResetController {

    @Autowired
    private PasswordResetService resetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordDto request) {
        try {
            resetService.createPasswordResetToken(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/reset-password/validate")
    public ResponseEntity<Void> validateToken(@RequestParam String token) {
        if (resetService.validatePasswordResetToken(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordDto request) {
        try {
            resetService.resetPassword(request);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
