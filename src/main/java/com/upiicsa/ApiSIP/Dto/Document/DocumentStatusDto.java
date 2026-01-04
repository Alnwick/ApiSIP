package com.upiicsa.ApiSIP.Dto.Document;

public record DocumentStatusDto(
  String typeCode,
  String status,
  String fileName,
  String comment,
  String viewUrl
) {
}
