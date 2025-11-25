package com.upiicsa.ApiSIP.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIP_TIPOUSUARIO")
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TIPOUSUARIO")
    private Integer id;

    @Column(name = "DESCRIPCION", length = 80)
    private String descripcion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SIP_TIPOUSU_PERMISOS",
            joinColumns = @JoinColumn(name = "ID_TIPOUSUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_PERMISO")
    )
    @ToString.Exclude
    private Set<Permiso> permisos = new HashSet<>();
}
