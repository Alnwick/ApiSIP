package com.upiicsa.ApiSIP.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "SIP_USUARIOS")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserSIP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "NOMBRE", length = 60)
    private String firstName;

    @Column(name = "PATERNO", length = 60)
    private String lastName;

    @Column(name = "MATERNO", length = 60)
    private String middleName;

    @Column(name = "CORREO", length = 100, unique = true)
    private String email;

    @Column(name = "CONTRASENA", length = 100)
    private String password;

    @Column(name = "HABILITADO")
    private Boolean enabled;

    @Column(name = "FECHA_ALTA")
    private LocalDateTime createdAt;

    @Column(name = "FECHA_BAJA")
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "ID_TIPOUSUARIO", referencedColumnName = "ID_TIPOUSUARIO")
    private TipoUsuario userType;

    // Relación con SIP_CESTATUS (Asumida como Status)
    @ManyToOne
    @JoinColumn(name = "ID_ESTATUS", referencedColumnName = "ID_C_ESTATUS")
    private Estatus estatus;
}
