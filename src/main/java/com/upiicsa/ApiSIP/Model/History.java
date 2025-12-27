package com.upiicsa.ApiSIP.Model;

import com.upiicsa.ApiSIP.Model.Catalogs.ProcessState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
@Table(name = "SIP_HISTORIAL")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HISTORIAL")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_PROCESO")
    private StudentProcess process;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private UserSIP user;

    @Column(name = "FECHA_ACT")
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "ESTADO_NUEVO", referencedColumnName = "ID_ESTPROCESO")
    private ProcessState newState;

    @ManyToOne
    @JoinColumn(name = "ESTADO_ANTERIOR", referencedColumnName = "ID_ESTPROCESO")
    private ProcessState oldState;
}
