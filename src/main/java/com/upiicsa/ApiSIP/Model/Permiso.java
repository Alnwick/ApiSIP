package com.upiicsa.ApiSIP.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "SIP_PERMISOS")
public class Permisos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERMISO")
    private Long id;

    @Column(name = "DESCRIPCION", length = 80)
    private String descripcion;
}
