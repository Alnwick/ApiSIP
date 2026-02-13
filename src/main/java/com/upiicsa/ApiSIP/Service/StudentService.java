package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.Student.StudentNameDto;
import com.upiicsa.ApiSIP.Dto.Student.StudentProfileDto;
import com.upiicsa.ApiSIP.Dto.Student.StudentRegistrationDto;
import com.upiicsa.ApiSIP.Exception.ValidationException;
import com.upiicsa.ApiSIP.Model.Document_Process.StudentProcess;
import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Repository.*;
import com.upiicsa.ApiSIP.Service.Auth.EmailVerificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class StudentService {

    private StudentRepository studentRepository;
    private PasswordEncoder passwordEncoder;
    private EmailVerificationService verificationService;
    private CatalogsService catalogsService;
    private StudentProcessService processService;

    public StudentService (StudentRepository studentRepository, PasswordEncoder passwordEncoder,
                           EmailVerificationService verificationService, CatalogsService catalogsService,
                           StudentProcessService processService) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationService = verificationService;
        this.catalogsService = catalogsService;
        this.processService = processService;
    }

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
                .userType(catalogsService.getType("ALUMNO"))
                .status(catalogsService.getStatus("ACTIVO"))
                .enrollment(registrationDto.enrollment()).phone(registrationDto.phone())
                .semester(catalogsService.getSemester(registrationDto))
                .graduate(registrationDto.graduated())
                .offer(catalogsService.getOffer(registrationDto))
                .build();

        studentRepository.save(newStudent);
        verificationService.createAndSendConfirmationCode(newStudent);
        processService.setFirstState(newStudent);

        return newStudent;
    }

    @Transactional(readOnly = true)
    public StudentProfileDto getProfile(Integer id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        StudentProcess process= processService.getByStudentId(student.getId());

        String semester;
        if(student.isGraduate()){
            semester = "PASANTE";
        }else {
            semester = student.getSemester().getDescription();
        }

        StudentProfileDto profileDto = new StudentProfileDto(student.getName(), student.getFLastName(),
                student.getMLastName(), student.getEnrollment(), student.getEmail(), student.getPhone(),
                student.getOffer().getCareer().getName(), student.getOffer().getSyllabus().code, semester,
                process.getProcessState().getDescription());

        System.out.println(profileDto);
        return profileDto;
    }

    @Transactional(readOnly = true)
    public StudentNameDto getName(Integer id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        String name  = student.getName();
        String splitName;
        int space =  name.indexOf(" ");

        if(space != -1) {
            splitName =  name.substring(0, space);
        }else {
            splitName = name;
        }

        return new StudentNameDto(splitName, student.getFLastName());
    }
}
