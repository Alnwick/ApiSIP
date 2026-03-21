package com.upiicsa.ApiSIP.Dto.Data;

public record ProcessProgressDto(
        String stageName,
        String date,
        boolean isCurrent
) {
}
