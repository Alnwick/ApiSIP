package com.upiicsa.ApiSIP.Model.Catalogs;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
public abstract class BaseCatalog {

    @Column(name = "DESCRIPCION", length = 100)
    private String description;

}
