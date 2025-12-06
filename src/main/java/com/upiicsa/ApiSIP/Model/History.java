package com.upiicsa.ApiSIP.Model.Token_Restore;

import com.upiicsa.ApiSIP.Model.Catalogs.ProcessState;
import com.upiicsa.ApiSIP.Model.StudentProcess;
import com.upiicsa.ApiSIP.Model.UserSIP;
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
    @JoinColumn(name = "ID_ALUMNOPROC")
    private StudentProcess process;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIORESP")
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
