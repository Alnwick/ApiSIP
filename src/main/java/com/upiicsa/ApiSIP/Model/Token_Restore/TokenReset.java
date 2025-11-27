package com.upiicsa.ApiSIP.Model.Token_Restore;

import com.upiicsa.ApiSIP.Model.UserSIP;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIP_TOKEN_RESETEO")
public class TokenReseteo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TOKEN_RESETEO")
    private Integer id;

    @Column(name = "TOKEN", length = 200)
    private String token;

    @Column(name = "FECHA_EXPIRACION")
    private LocalDateTime fechaExpiracion;

    @Column(name = "FECHA_USO")
    private LocalDateTime fechaUso;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private UserSIP usuario;
}
