package com.upiicsa.ApiSIP.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "SIP_TIPOUSUARIO")
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TIPOUSUARIO")
    private Long id;

    @Column(name = "DESCRIPCION", length = 80)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SIP_TIPOUSU-PERMISOS",
            joinColumns = @JoinColumn(name = "ID_TIPOUSUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_PERMISO")
    )
    @ToString.Exclude
    private Set<Permisos> permissions = new HashSet<>();
}
