package com.upiicsa.ApiSIP.Dto.Catalogs;

import com.upiicsa.ApiSIP.Model.Catalogs.Syllabus;

public record SyllabusDto(
        String code
) {
    public SyllabusDto(Syllabus s){
        this(s.getCode());
    }
}
