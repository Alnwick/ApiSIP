package com.upiicsa.ApiSIP.Dto.Cedula;

public record CedulaDto(
        AddressDto studentAddress,
        CompanyDto companyInfo,
        AddressDto companyAddress
) {
}
