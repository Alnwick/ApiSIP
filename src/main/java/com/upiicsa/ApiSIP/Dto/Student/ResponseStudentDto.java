package com.upiicsa.ApiSIP.Dto.Student;

import com.upiicsa.ApiSIP.Model.Student;

public record ResponseStudentDto(
    String name,
    String fLastName,
    String mLastName,
    String enrollment,
    String syllabusCode
    //String processStatus
) {
    public ResponseStudentDto(Student s){
        this(s.getName(), s.getFLastName(), s.getMLastName(), s.getEnrollment(),
                s.getOffer().getSyllabus().code);
    }
}