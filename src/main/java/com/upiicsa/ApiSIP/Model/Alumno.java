package com.upiicsa.ApiSIP.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SIP_ALUMNOS")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
public class Alumno extends Usuario {

    @Column(name = "MATRICULA", length = 20, unique = true)
    private String matricula;

    @Column(name = "TELEFONO", length = 20)
    private String phone;

    @Column(name = "EGRESADO")
    private Boolean isGraduated;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_DIRECCION")
    private Direccion direccion;

    @ManyToOne
    @JoinColumn(name = "ID_OFERTA")
    private OfertaAca ofertaAca;
}
