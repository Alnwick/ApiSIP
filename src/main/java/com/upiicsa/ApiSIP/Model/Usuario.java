package com.upiicsa.ApiSIP.Model;

import com.upiicsa.ApiSIP.Model.Catalogs.Estatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "SIP_USUARIOS")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Integer id;

    @Column(name = "NOMBRE", length = 60)
    private String nombre;

    @Column(name = "PATERNO", length = 60)
    private String paterno;

    @Column(name = "MATERNO", length = 60)
    private String materno;

    @Column(name = "CORREO", length = 100, unique = true)
    private String correo;

    @Column(name = "CONTRASENA", length = 100)
    private String contrasena;

    @Column(name = "HABILITADO")
    private Boolean habilitado;

    @Column(name = "FECHA_ALTA")
    private LocalDateTime fechaAlta;

    @Column(name = "FECHA_BAJA")
    private LocalDateTime fechaBaja;

    @ManyToOne
    @JoinColumn(name = "ID_TIPOUSUARIO")
    private TipoUsuario tipoUsuario;

    @ManyToOne
    @JoinColumn(name = "ID_ESTATUS")
    private Estatus estatus;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        //Agregar permisos
        this.tipoUsuario.getPermisos()
                .stream()
                .map(permiso ->  new SimpleGrantedAuthority(permiso.getDescripcion()))
                .forEach(authorities::add);
        //Agregar tipo de Usuario
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.tipoUsuario.getDescripcion()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.contrasena;
    }

    @Override
    public String getUsername() {
        return this.correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.habilitado;
    }
}
