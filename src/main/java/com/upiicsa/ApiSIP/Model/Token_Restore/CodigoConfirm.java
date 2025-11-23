package com.upiicsa.ApiSIP.Model.Token_Restore;

import com.upiicsa.ApiSIP.Model.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SIP_CODIGO_CONFIRM")
public class CodigoConfirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "CODIGO", length = 10)
    private String codigo;

    @Column(name = "FECHA_EXPIRACION")
    private LocalDateTime fechaExpiracion;

    @Column(name = "FECHA_USO")
    private LocalDateTime fechaUso;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;
}
