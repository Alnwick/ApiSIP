package com.upiicsa.ApiSIP.Dto.Document;

import java.time.LocalDateTime;

public record DocumentStatusDto(
        String typeCode,
        String status,
        String fileName,
        String comment,
        String viewUrl,
        LocalDateTime uploadDate
) {
}
