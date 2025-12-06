package com.upiicsa.ApiSIP.Dto.Auth;

public record AuthResponseDto(
        String message,
        String userType,
        boolean flag
) {
}
