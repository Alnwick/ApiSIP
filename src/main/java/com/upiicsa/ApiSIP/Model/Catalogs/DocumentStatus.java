package com.upiicsa.ApiSIP.Model.Catalogs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIP_CESTDODOC")
public class DocumentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESTDODOC")
    private Integer id;

    @Column(name = "DESCRIPCION", length = 100)
    private String description;
}
