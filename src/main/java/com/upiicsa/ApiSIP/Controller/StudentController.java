package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Dto.Email.EmailConfirmDto;
import com.upiicsa.ApiSIP.Dto.Student.ResponseStudentDto;
import com.upiicsa.ApiSIP.Dto.Student.StudentRegistrationDto;
import com.upiicsa.ApiSIP.Model.Alumno;
import com.upiicsa.ApiSIP.Service.EmailVerificationService;
import com.upiicsa.ApiSIP.Service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/estudiante")
public class StudentController {

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private EmailVerificationService verificationService;

    @PostMapping("/register")
    public ResponseEntity<ResponseStudentDto> registerUser(@RequestBody @Valid StudentRegistrationDto registrationDto) {

        Alumno alumno = estudianteService.registerStudent(registrationDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(alumno.getId()).toUri();

        return ResponseEntity.created(location).body(new ResponseStudentDto(alumno));
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<Void> confirmEmail(@RequestBody @Valid EmailConfirmDto emailConfirmation) {
        verificationService.confirmEmail(emailConfirmation);

        return ResponseEntity.ok().build();
    }
}
