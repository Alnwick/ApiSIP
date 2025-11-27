package com.upiicsa.ApiSIP.Model.Token_Restore;

import com.upiicsa.ApiSIP.Model.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIP_DURACION_TOKEN")
public class DuracionToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DURACION")
    private Integer id;

    @Column
    private Integer duracionHoras;

    @ManyToOne
    @JoinColumn(name = "ID_TIPOUSUARIO")
    private UserType userType;
}
