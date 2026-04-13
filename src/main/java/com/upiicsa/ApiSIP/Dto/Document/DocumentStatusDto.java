package com.upiicsa.ApiSIP.Dto.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record DocumentStatusDto(
        @JsonProperty("typeCode")
        String typeName,
        String status,
        String fileName,
        String comment,
        String viewUrl,
        LocalDateTime uploadDate
) {
}
