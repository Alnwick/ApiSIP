package com.upiicsa.ApiSIP.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "SIP_DIRECCION")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DIRECCION")
    private Integer id;

    @Column(name = "CALLE", length = 100)
    private String calle;

    @Column(name = "NUMERO", length = 5)
    private String numero;

    @Column(name = "COLONIA", length = 100)
    private String colonia;

    @Column(name = "CODIGOP", length = 10)
    private String codigoPostal;

    @Column(name = "ENTIDADFED", length = 100)
    private String entidadFed;
}
