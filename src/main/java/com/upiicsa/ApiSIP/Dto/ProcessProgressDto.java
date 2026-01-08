package com.upiicsa.ApiSIP.Dto;

public record ProcessProgressDto(
        String stageName,
        String date,
        boolean isCurrent
) {
}
