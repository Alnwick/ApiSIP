package com.upiicsa.ApiSIP.Model.Catalogs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIP_ESCUELAS")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESCUELA")
    private Integer id;

    @Column(name = "NOMBRE", length = 100)
    private String name;

    @Column(name = "SIGLAS", length = 30)
    private String acronym;
}
