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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TypeUserRepository typeUserRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private EmailVerificationService verificationService;

    public Page<Student> getStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Transactional
    public Student registerStudent(StudentRegistrationDto registrationDto) {
        if (!registrationDto.password().equals(registrationDto.confirmPassword()))
            throw new ValidationException("Invalid password");

        Semester semester = null;
        UserType tipo = typeUserRepository.findByDescription("ALUMNO")
                .orElseThrow(() -> new ResourceNotFoundException("TipoUsuario not found"));
        Status status = statusRepository.findByDescription("ACTIVO")
                .orElseThrow(() -> new ResourceNotFoundException("Estatus not found"));
        Offer offer = offerRepository.findByCompositeKeys(
                registrationDto.schoolName(), registrationDto.acronymCareer(), registrationDto.syllabusCode())
                .orElseThrow(() -> new ResourceNotFoundException("OfertaAca not found"));

        if(!registrationDto.graduated()){
             semester = semesterRepository.findByDescription(registrationDto.semester())
                    .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));
        }

        Student newStudent = Student.builder()
                .email(registrationDto.email())
                .password(passwordEncoder.encode(registrationDto.password()))
                .fLastName(registrationDto.fLastName()).mLastName(registrationDto.mLastName())
                .name(registrationDto.name())
                .enabled(false).registrationDate(LocalDateTime.now())
                .userType(tipo).status(status)
                .enrollment(registrationDto.enrollment()).phone(registrationDto.phone())
                .semester(semester).graduate(registrationDto.graduated())
                .offer(offer)
                .build();

        studentRepository.save(newStudent);
        verificationService.createAndSendConfirmationCode(newStudent);

        return newStudent;
    }
}
