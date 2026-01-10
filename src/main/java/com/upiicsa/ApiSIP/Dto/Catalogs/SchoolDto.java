package com.upiicsa.ApiSIP.Dto.Catalogs;

import com.upiicsa.ApiSIP.Model.Catalogs.School;

public record SchoolDto(
        String name,
        String acronym
) {
    public SchoolDto(School s) {
        this(s.getName(), s.getAcronym());
    }
}
