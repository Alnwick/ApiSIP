package com.upiicsa.ApiSIP.Dto.Student;

import com.upiicsa.ApiSIP.Dto.Document.DocumentStatusDto;

import java.util.List;

public record StudentReviewDto(
        String name,
        String enrollment,
        String career,
        String semester,
        String syllabus,
        List<DocumentStatusDto> documents
) {
}
