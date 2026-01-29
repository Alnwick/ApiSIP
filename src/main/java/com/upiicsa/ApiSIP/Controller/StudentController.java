package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Dto.CedulaDto;
import com.upiicsa.ApiSIP.Dto.Email.EmailConfirmDto;
import com.upiicsa.ApiSIP.Dto.ProcessProgressDto;
import com.upiicsa.ApiSIP.Dto.Student.ResponseStudentDto;
import com.upiicsa.ApiSIP.Dto.Student.StudentRegistrationDto;
import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Service.CedulaService;
import com.upiicsa.ApiSIP.Service.EmailVerificationService;
import com.upiicsa.ApiSIP.Service.StudentProcessService;
import com.upiicsa.ApiSIP.Service.StudentService;
import com.upiicsa.ApiSIP.Utils.AuthHelper;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;
    private EmailVerificationService verificationService;
    private StudentProcessService studentProcessService;
    private CedulaService cedulaService;

    public StudentController(StudentService studentService, EmailVerificationService verificationService,
                             StudentProcessService studentProcessService, CedulaService cedulaService) {
        this.studentService = studentService;
        this.verificationService = verificationService;
        this.studentProcessService = studentProcessService;
        this.cedulaService = cedulaService;
    }

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

    @GetMapping("/process-status")
    @PreAuthorize("hasAnyRole('ALUMNO')")
    public ResponseEntity<List<ProcessProgressDto>> getProcessStatus() {
        return ResponseEntity.ok(studentProcessService.
                getProcessHistory(AuthHelper.getAuthenticatedUserId()));
    }

    @GetMapping("/cedula-data")
    @PreAuthorize("hasAnyRole('ALUMNO')")
    public ResponseEntity<CedulaDto> getCedulaData() {
        CedulaDto data = cedulaService.getAllData(AuthHelper.getAuthenticatedUserId());

        return ResponseEntity.ok(data);
    }

    @PostMapping("/generate-cedula")
    @PreAuthorize("hasAnyRole('ALUMNO')")
    public ResponseEntity<String> generateCedula(@RequestBody @Valid CedulaDto cedulaDto) {
        Integer studentId = AuthHelper.getAuthenticatedUserId();

        return ResponseEntity.ok(cedulaService.generateCedula(studentId, cedulaDto));
    }

    @GetMapping("/view-cedula-pdf")
    @PreAuthorize("hasAnyRole('ALUMNO')")
    public ResponseEntity<Resource>  viewCedulaPdf(){
        Integer studentId = AuthHelper.getAuthenticatedUserId();

        return cedulaService.getPdfResponseEntity(studentId);
    }
}
