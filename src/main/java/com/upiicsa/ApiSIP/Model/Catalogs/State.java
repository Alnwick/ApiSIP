package com.upiicsa.ApiSIP.Model.Catalogs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SIP_EDOS")
public class State {

    @Id
    @Column(name = "ID_EDO")
    private Integer id;

    @Column(name = "NOMBRE")
    private String name;
}
