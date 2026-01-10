package com.upiicsa.ApiSIP.Dto.Catalogs;

import com.upiicsa.ApiSIP.Model.Catalogs.Semester;

public record SemesterDto(
        String description
) {
    public SemesterDto(Semester s) {
        this(s.getDescription());
    }
}
