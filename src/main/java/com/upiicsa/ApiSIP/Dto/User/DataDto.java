package com.upiicsa.ApiSIP.Dto.User;

import com.upiicsa.ApiSIP.Dto.Student.InfoInstitutionalDto;

public record DataDto(
        String name,
        String fLastName,
        String mLastName,
        String email,
        InfoInstitutionalDto infoInstitutional
) {
}
