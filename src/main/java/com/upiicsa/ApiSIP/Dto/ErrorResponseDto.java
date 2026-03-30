package com.upiicsa.ApiSIP.Dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        LocalDateTime dateTime,
        int status,
        String error,
        String message
) {
}
