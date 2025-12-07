package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operatives")
public class OperativeController {

    @Autowired
    private StudentService studentService;

    @RequestMapping("/get-allStudents")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATIVE')")
    public ResponseEntity<Page<Student>> getAllStudents(Pageable pageable) {
        Page<Student> students = studentService.getStudents(pageable);

        return ResponseEntity.ok(students);
    }
}
