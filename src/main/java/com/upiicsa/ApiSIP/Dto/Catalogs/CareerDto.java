package com.upiicsa.ApiSIP.Dto.Catalogs;

import com.upiicsa.ApiSIP.Model.Catalogs.Career;

public record CareerDto(
        String name,
        String acronym
) {
    public CareerDto(Career c){
        this(c.getName(), c.getAcronym());
    }
}
