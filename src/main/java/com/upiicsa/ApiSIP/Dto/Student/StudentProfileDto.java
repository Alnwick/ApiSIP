package com.upiicsa.ApiSIP.Dto.Student;

import com.upiicsa.ApiSIP.Model.Document_Process.StudentProcess;
import com.upiicsa.ApiSIP.Model.Offer;
import com.upiicsa.ApiSIP.Model.Student;

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
