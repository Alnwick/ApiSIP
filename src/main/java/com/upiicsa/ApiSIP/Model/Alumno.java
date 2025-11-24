package com.upiicsa.ApiSIP.Model;

import com.upiicsa.ApiSIP.Model.Catalogs.Semestre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SIP_ALUMNOS")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
public class Alumno extends Usuario {

    @Column(name = "MATRICULA", length = 20, unique = true)
    private String matricula;

    @Column(name = "TELEFONO", length = 20)
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "ID_SEMESTRE")
    private Semestre semestre;

    @Column(name = "EGRESADO")
    private boolean egresado;

    @ManyToOne
    @JoinColumn(name = "ID_DIRECCION")
    private Direccion direccion;

    @ManyToOne
    @JoinColumn(name = "ID_OFERTA")
    private OfertaAca ofertaAca;
}
