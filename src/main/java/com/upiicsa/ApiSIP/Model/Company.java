package com.upiicsa.ApiSIP.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SIP_EMPRESA")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPRESA")
    public Integer id;

    @Column(name = "RAZON_SOCIAL", length = 100)
    private String name;

    @Column(name = "CORREO", length = 100)
    private String email;

    @Column(name = "SECTOR", length = 20)
    private String sector;

    @Column(name = "TELEFONO", length = 45)
    private String phone;

    @Column(name = "EXTENSION", length = 10)
    private String extension;

    @Column(name = "FAX", length = 45)
    private String fax;

    @Column(name = "RESPONSABLE", length = 45)
    private String supervisor;

    @Column(name = "GRADO_ACA_REPO", length = 10)
    private String supervisorGrade;

    @Column(name = "PUESTO_RESPO", length = 100)
    private String positionSupervisor;

    @Column(name = "PUESTO_ALUMNO", length = 100)
    private String positionStudent;

    @ManyToOne
    @JoinColumn(name = "ID_DIRECCION")
    private Address address;
}
