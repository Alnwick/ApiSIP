package com.upiicsa.ApiSIP.Dto;

import com.upiicsa.ApiSIP.Model.Address;

public record AddressDto(
        String street,
        String number,
        String zipCode,
        String colony,
        String neighborhood,
        Integer stateId
) {
    public AddressDto(Address a){
        this(a.getStreet(), a.getNumber(), a.getZipCode(), a.getNeighborhood(), a.getNeighborhood(), a.getState().getId());
    }
}
