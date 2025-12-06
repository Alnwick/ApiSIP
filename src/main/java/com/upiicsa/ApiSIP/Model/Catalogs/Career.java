package com.upiicsa.ApiSIP.Model.Catalogs;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIP_CARRERAS")
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CARRERA")
    private Integer id;

    @Column(name = "NOMBRE", length = 100)
    private String name;

    @Column(name = "SIGLAS", length = 30)
    private String acronym;
}
