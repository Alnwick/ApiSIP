package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Dto.Email.EmailConfirmDto;
import com.upiicsa.ApiSIP.Dto.Student.ResponseStudentDto;
import com.upiicsa.ApiSIP.Dto.Student.StudentRegistrationDto;
import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Service.EmailVerificationService;
import com.upiicsa.ApiSIP.Service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EmailVerificationService verificationService;

    @PostMapping("/register")
    public ResponseEntity<ResponseStudentDto> registerUser(@RequestBody @Valid StudentRegistrationDto registrationDto) {

        Student student = studentService.registerStudent(registrationDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(student.getId()).toUri();

        return ResponseEntity.created(location).body(new ResponseStudentDto(student));
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<Void> confirmEmail(@RequestBody @Valid EmailConfirmDto emailConfirmation) {
        verificationService.confirmEmail(emailConfirmation);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-code")
    public ResponseEntity<String> resendCode(@RequestParam("email") String email) {

        verificationService.resendConfirmationCode(email);
        return ResponseEntity.ok("Código reenviado correctamente.");
    }
}
