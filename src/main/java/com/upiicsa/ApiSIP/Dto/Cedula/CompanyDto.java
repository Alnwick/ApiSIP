package com.upiicsa.ApiSIP.Dto.Cedula;

import com.upiicsa.ApiSIP.Model.Company;

public record CompanyDto(
        String name,
        String email,
        String phone,
        String extension,
        String fax,
        String sector,
        String supervisorGrade,
        String supervisor,
        String positionSupervisor,
        String positionStudent
) {
    public CompanyDto(Company c){
        this(c.getName(), c.getEmail(), c.getPhone(), c.getExtension(), c.getFax(), c.getSector(),
                c.getSupervisorGrade(), c.getSupervisor(), c.getPositionSupervisor(), c.getPositionStudent());
    }
}
