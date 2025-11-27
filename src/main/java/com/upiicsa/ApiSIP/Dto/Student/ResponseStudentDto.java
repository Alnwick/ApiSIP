package com.upiicsa.ApiSIP.Dto.Student;

import com.upiicsa.ApiSIP.Model.Student;

public record ResponseStudentDto(
    String fLastName,
    String email,
    String enrollment
) {
    public ResponseStudentDto(Student alumno) {
        this(alumno.getFLastName(), alumno.getEmail(), alumno.getEnrollment());
    }
}