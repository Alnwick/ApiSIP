package com.upiicsa.ApiSIP.Dto;

import com.upiicsa.ApiSIP.Dto.Student.InfoInstitutionalDto;

public record ProfileDto(
        String name,
        String fLastName,
        String mLastName,
        String email,
        String phone,
        InfoInstitutionalDto infoInstitutional
) {
}
