package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.Email.EmailConfirmDto;
import com.upiicsa.ApiSIP.Model.Token_Restore.ConfirmationCode;
import com.upiicsa.ApiSIP.Model.UserSIP;
import com.upiicsa.ApiSIP.Repository.Token_Restore.ConfirmationCodeRepository;
import com.upiicsa.ApiSIP.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailVerificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Transactional
    public void createAndSendConfirmationCode(UserSIP usuario) {

        // Generate code to 6 random digits
        String code = String.format("%06d", new Random().nextInt(1000000));

        ConfirmationCode codigo = new ConfirmationCode(null, code,
                LocalDateTime.now().plusMinutes(30), null, usuario);
        confirmationCodeRepository.save(codigo);


        emailService.sendConfirmationCode(usuario.getEmail(), code);

        System.out.println("\n------------------------------------------------------");
        System.out.println(">>> CÓDIGO DE CONFIRMACIÓN GENERADO: " + code);
        System.out.println(">>> ENVIADO A: " + usuario.getEmail());
        System.out.println("------------------------------------------------------\n");
    }

    @Transactional
    public void confirmEmail(EmailConfirmDto emailConfirmation) {
        var token = confirmationCodeRepository.findByCode(emailConfirmation.code())
                .orElseThrow(() -> new IllegalArgumentException("Código de confirmación inválido."));

        UserSIP usuario = token.getUser();

        if(usuario.getEmail().equals(emailConfirmation.email())){
            if (token.getExpirationDate().isBefore(LocalDateTime.now())) {
                //codigoConfirmRepository.delete(token);
                throw new IllegalArgumentException("El código de confirmación ha expirado. Por favor, solicite uno nuevo.");
            }
            if (usuario.isEnabled()) {
                return;
            }
        }else {
            throw new IllegalArgumentException("El codigo no corresponde al email del usuario");
        }

        usuario.setEnabled(true);
        userRepository.save(usuario);

        token.setUseDate(LocalDateTime.now());
        confirmationCodeRepository.save(token);
    }
}
