package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.Student.StudentRegistrationDto;
import com.upiicsa.ApiSIP.Exception.ResourceNotFoundException;
import com.upiicsa.ApiSIP.Exception.ValidationException;
import com.upiicsa.ApiSIP.Model.Alumno;
import com.upiicsa.ApiSIP.Model.Catalogs.Estatus;
import com.upiicsa.ApiSIP.Model.Catalogs.Semestre;
import com.upiicsa.ApiSIP.Model.OfertaAca;
import com.upiicsa.ApiSIP.Model.TipoUsuario;
import com.upiicsa.ApiSIP.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Autowired
    private EstatusRepository estatusRepository;

    @Autowired
    private SemestreRepository semestreRepository;

    @Autowired
    private OfertaAcaRepository ofertaAcaRepository;

    @Autowired
    private EmailVerificationService verificationService;

    @Transactional
    public Alumno registerStudent(StudentRegistrationDto registrationDto) {
        if (!registrationDto.contrasena().equals(registrationDto.confirmarContrasena()))
            throw new ValidationException("Invalid password");

        TipoUsuario tipo = tipoUsuarioRepository.findByDescripcion("ALUMNO")
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario not found"));
        Estatus estatus = estatusRepository.findByDescripcion("ACTIVO")
                .orElseThrow(() -> new ResourceNotFoundException("Estatus not found"));
        Semestre semestre = semestreRepository.findByDescripcion(registrationDto.semestreDes())
                .orElseThrow(() -> new ResourceNotFoundException("Semestre not found"));
        OfertaAca ofertaAca = ofertaAcaRepository.findByCompositeKeys(
                registrationDto.escuelaNom(), registrationDto.carreraNom(), registrationDto.planEstCodigo())
                .orElseThrow(() -> new ResourceNotFoundException("OfertaAca not found"));

        Alumno newAlumno = Alumno.builder()
                .correo(registrationDto.correo())
                .contrasena(passwordEncoder.encode(registrationDto.contrasena()))
                .paterno(registrationDto.paterno()).materno(registrationDto.materno())
                .nombre(registrationDto.nombre())
                .habilitado(false).fechaAlta(LocalDateTime.now())
                .tipoUsuario(tipo).estatus(estatus)
                .matricula(registrationDto.matricula()).telefono(registrationDto.telefono())
                .semestre(semestre)
                .ofertaAca(ofertaAca)
                .build();

        estudianteRepository.save(newAlumno);
        verificationService.createAndSendConfirmationCode(newAlumno);

        return newAlumno;
    }
}
