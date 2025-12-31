package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.Student.StudentRegistrationDto;
import com.upiicsa.ApiSIP.Exception.ValidationException;
import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailVerificationService verificationService;

    @Autowired
    private DataForStudentService dataService;

    @Autowired
    private StudentProcessService processService;

    public Page<Student> getStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Transactional
    public Student registerStudent(StudentRegistrationDto registrationDto) {
        if (!registrationDto.password().equals(registrationDto.confirmPassword()))
            throw new ValidationException("Invalid password");

        Student newStudent = Student.builder()
                .email(registrationDto.email())
                .password(passwordEncoder.encode(registrationDto.password()))
                .fLastName(registrationDto.fLastName()).mLastName(registrationDto.mLastName())
                .name(registrationDto.name())
                .enabled(false).registrationDate(LocalDateTime.now())
                .userType(dataService.getType("ALUMNO"))
                .status(dataService.getStatus("ACTIVO"))
                .enrollment(registrationDto.enrollment()).phone(registrationDto.phone())
                .semester(dataService.getSemester(registrationDto))
                .graduate(registrationDto.graduated())
                .offer(dataService.getOffer(registrationDto))
                .build();

        studentRepository.save(newStudent);
        verificationService.createAndSendConfirmationCode(newStudent);
        processService.setFirstState(newStudent);

        return newStudent;
    }
}
