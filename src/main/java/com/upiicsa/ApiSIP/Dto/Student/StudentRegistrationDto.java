package com.upiicsa.ApiSIP.Dto.Student;

public record StudentRegistrationDto(
        String email,
        String fLastName,
        String mLastName,
        String name,
        String password,
        String confirmPassword,
        String enrollment,
        String phone,
        String semester,
        boolean graduated,
        String schoolName,
        String acronymCareer,
        String syllabusCode
) {
}
