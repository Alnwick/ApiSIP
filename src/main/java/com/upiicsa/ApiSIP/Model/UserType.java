package com.upiicsa.ApiSIP.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIP_CTIPOUSUARIO")
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TIPOUSUARIO")
    private Integer id;

    @Column(name = "DESCRIPCION", length = 80)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SIP_TIPOUSU_PERMISOS",
            joinColumns = @JoinColumn(name = "ID_TIPOUSUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_PERMISO")
    )
    @ToString.Exclude
    private Set<Permission> permissions = new HashSet<>();
}
