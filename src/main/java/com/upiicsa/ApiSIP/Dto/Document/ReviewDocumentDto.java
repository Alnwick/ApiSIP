package com.upiicsa.ApiSIP.Dto.Document;

public record ReviewDocumentDto(
        String typeName,
        Boolean approved,
        String comment
) {
}
