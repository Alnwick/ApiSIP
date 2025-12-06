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
@Table(name = "SIP_ALUMNOPROC")
public class StudentProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALUMNOPROC")
    private Integer id;

    @Column(name = "FECHA_INICIO")
    private LocalDateTime StartDate;

    @Column(name = "FECHA_FIN")
    private LocalDateTime EndDate;

    @Column(name = "ACTIVO")
    private Boolean Active;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "ID_CESTPROCESO")
    private ProcessState processState;

    @Column(name = "OBSERVACIONES", length = 150)
    private String observations;
}
