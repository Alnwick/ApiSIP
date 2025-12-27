package com.upiicsa.ApiSIP.Model;

import com.upiicsa.ApiSIP.Model.Catalogs.ProcessState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIP_PROCESO")
public class StudentProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROCESO")
    private Integer id;

    @Column(name = "FECHA_INICIO")
    private LocalDateTime StartDate;

    @Column(name = "FECHA_FIN")
    private LocalDateTime EndDate;

    @Column(name = "ACTIVO")
    private Boolean Active;

    @ManyToOne
    @JoinColumn(name = "ID_ALUMNO")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "ESTADO_ACTUAL", referencedColumnName = "ID_ESTPROCESO")
    private ProcessState processState;

    @Column(name = "OBSERVACIONES", length = 150)
    private String observations;

    @ManyToOne
    @JoinColumn(name = "ID_EMPRESA")
    private Company company;
}
