package com.upiicsa.ApiSIP.Dto.Student;

import com.upiicsa.ApiSIP.Model.Alumno;

public record ResponseStudentDto(
    String paterno,
    String correo,
    String matricula
) {
    public ResponseStudentDto(Alumno alumno) {
        this(alumno.getPaterno(), alumno.getCorreo(), alumno.getMatricula());
    }
}