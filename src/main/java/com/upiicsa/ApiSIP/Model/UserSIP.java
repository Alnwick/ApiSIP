package com.upiicsa.ApiSIP.Model;

import com.upiicsa.ApiSIP.Model.Catalogs.Status;
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
public class UserSIP implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Integer id;

    @Column(name = "NOMBRE", length = 60)
    private String name;

    @Column(name = "PATERNO", length = 60)
    private String fLastName;

    @Column(name = "MATERNO", length = 60)
    private String mLastName;

    @Column(name = "CORREO", length = 100, unique = true)
    private String email;

    @Column(name = "CONTRASENA", length = 100)
    private String password;

    @Column(name = "HABILITADO")
    private Boolean enabled;

    @Column(name = "FECHA_ALTA")
    private LocalDateTime registrationDate;

    @Column(name = "FECHA_BAJA")
    private LocalDateTime cancellationDate;

    @ManyToOne
    @JoinColumn(name = "ID_TIPOUSUARIO")
    private UserType userType;

    @ManyToOne
    @JoinColumn(name = "ID_ESTATUS")
    private Status status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        //Agregar permisos
        this.userType.getPermissions()
                .stream()
                .map(permiso ->  new SimpleGrantedAuthority(permiso.getDescription()))
                .forEach(authorities::add);
        //Agregar tipo de Usuario
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.userType.getDescription()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
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
        return this.enabled;
    }
}
