package com.upiicsa.ApiSIP.Dto;

import com.upiicsa.ApiSIP.Model.Address;

public record AddressDto(
        String street,
        String number,
        String zipCode,
        String neighborhood,
        Integer stateId
) {
    public AddressDto(Address a){
        this(a.getStreet(), a.getNumber(), a.getZipCode(), a.getNeighborhood(), a.getState().getId());
    }
}
