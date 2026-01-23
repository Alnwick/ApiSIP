package com.upiicsa.ApiSIP.Dto.Student;

import com.upiicsa.ApiSIP.Model.Student;

public record ResponseStudentDto(
    String fLastName,
    String email,
    String enrollment
) {
    public ResponseStudentDto(Student student) {
        this(student.getFLastName(), student.getEmail(), student.getEnrollment());
    }
}