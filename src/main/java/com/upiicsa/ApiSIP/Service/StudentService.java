package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.Student.StudentRegistrationDto;
import com.upiicsa.ApiSIP.Exception.ResourceNotFoundException;
import com.upiicsa.ApiSIP.Exception.ValidationException;
import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Model.Catalogs.Status;
import com.upiicsa.ApiSIP.Model.Catalogs.Semester;
import com.upiicsa.ApiSIP.Model.Offer;
import com.upiicsa.ApiSIP.Model.UserType;
import com.upiicsa.ApiSIP.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EstudianteService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TypeUserRepository typeUserRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private SemestreRepository semestreRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private EmailVerificationService verificationService;

    @Transactional
    public Student registerStudent(StudentRegistrationDto registrationDto) {
        if (!registrationDto.contrasena().equals(registrationDto.confirmarContrasena()))
            throw new ValidationException("Invalid password");

        Semester semester = null;
        UserType tipo = typeUserRepository.findByDescripcion("ALUMNO")
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario not found"));
        Status status = statusRepository.findByDescripcion("ACTIVO")
                .orElseThrow(() -> new ResourceNotFoundException("Estatus not found"));
        Offer offer = offerRepository.findByCompositeKeys(
                registrationDto.escuelaNom(), registrationDto.carreraNom(), registrationDto.planEstCodigo())
                .orElseThrow(() -> new ResourceNotFoundException("OfertaAca not found"));

        if(!registrationDto.egresado()){
             semester = semestreRepository.findByDescripcion(registrationDto.semestreDes())
                    .orElseThrow(() -> new ResourceNotFoundException("Semestre not found"));
        }

        Student newAlumno = Student.builder()
                .correo(registrationDto.correo())
                .contrasena(passwordEncoder.encode(registrationDto.contrasena()))
                .paterno(registrationDto.paterno()).materno(registrationDto.materno())
                .nombre(registrationDto.nombre())
                .habilitado(false).fechaAlta(LocalDateTime.now())
                .tipoUsuario(tipo).status(status)
                .matricula(registrationDto.matricula()).telefono(registrationDto.telefono())
                .semester(semester).egresado(registrationDto.egresado())
                .ofertaAca(offer)
                .build();

        studentRepository.save(newAlumno);
        verificationService.createAndSendConfirmationCode(newAlumno);

        return newAlumno;
    }
}
