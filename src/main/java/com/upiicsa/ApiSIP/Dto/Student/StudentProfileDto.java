package com.upiicsa.ApiSIP.Dto.Student;

public record StudentProfileDto(
        String name,
        String fLastName,
        String mLastName,
        String enrollment,
        String email,
        String phone,
        String career,
        String syllabus,
        String semester,
        String processStatus
) {
}
