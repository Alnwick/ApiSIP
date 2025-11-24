package com.upiicsa.ApiSIP.Dto.Student;

public record StudentRegistrationDto(
        String correo,
        String paterno,
        String materno,
        String nombre,
        String contrasena,
        String confirmarContrasena,
        String matricula,
        String telefono,
        String semestreDes,
        boolean egresado,
        String escuelaNom,
        String carreraNom,
        String planEstCodigo
) {
}
