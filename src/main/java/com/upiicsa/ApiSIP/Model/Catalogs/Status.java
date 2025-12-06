package com.upiicsa.ApiSIP.Model.Catalogs;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "SIP_CESTATUS")
public class Status extends BaseCatalog{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESTATUS")
    private Integer id;
}
