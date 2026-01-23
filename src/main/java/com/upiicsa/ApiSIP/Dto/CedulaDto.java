package com.upiicsa.ApiSIP.Dto;

public record CedulaDto(
        AddressDto studentAddress,
        CompanyDto companyInfo,
        AddressDto companyAddress
) {
}
