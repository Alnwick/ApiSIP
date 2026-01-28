package com.upiicsa.ApiSIP.Dto.Catalogs;

import com.upiicsa.ApiSIP.Model.Catalogs.State;

public record StateDto(
        Integer id,
        String name
) {
    public StateDto(State s){
        this(s.getId(), s.getName());
    }
}
