package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.Email.EmailConfirmDto;
import com.upiicsa.ApiSIP.Model.Token_Restore.CodigoConfirm;
import com.upiicsa.ApiSIP.Model.Usuario;
import com.upiicsa.ApiSIP.Repository.Token_Restore.CodigoConfirmRepository;
import com.upiicsa.ApiSIP.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailVerificationService {

    @Autowired
    private UsuarioRepository usuRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CodigoConfirmRepository codigoConfirmRepository;

    @Transactional
    public void createAndSendConfirmationCode(Usuario usuario) {

        //confirmationTokenRepository.findByUserId(user.getId()).ifPresent(confirmationTokenRepository::delete);

        // Generate code to 6 random digits
        String code = String.format("%06d", new Random().nextInt(1000000));

        CodigoConfirm codigo = new CodigoConfirm(null, code,  LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30), usuario);
        codigoConfirmRepository.save(codigo);


        emailService.sendConfirmationCode(usuario.getCorreo(), code);

        System.out.println("\n------------------------------------------------------");
        System.out.println(">>> CÓDIGO DE CONFIRMACIÓN GENERADO: " + code);
        System.out.println(">>> ENVIADO A: " + usuario.getCorreo());
        System.out.println("------------------------------------------------------\n");
    }

    @Transactional
    public void confirmEmail(EmailConfirmDto emailConfirmation) {
        var token = codigoConfirmRepository.findByCodigo(emailConfirmation.code())
                .orElseThrow(() -> new IllegalArgumentException("Código de confirmación inválido."));

        Usuario usuario = token.getUsuario();

        if(usuario.getCorreo().equals(emailConfirmation.email())){
            if (token.getFechaExpiracion().isBefore(LocalDateTime.now())) {
                //codigoConfirmRepository.delete(token);
                throw new IllegalArgumentException("El código de confirmación ha expirado. Por favor, solicite uno nuevo.");
            }
            if (usuario.isEnabled()) {
                //codigoConfirmRepository.delete(token);
                return;
            }
        }else {
            throw new IllegalArgumentException("El codigo no corresponde al email del usuario");
        }

        usuario.setHabilitado(true);
        usuRepository.save(usuario);

        //codigoConfirmRepository.delete(token);
    }
}
