package com.upiicsa.ApiSIP.Model.Catalogs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
@Table(name = "SIP_CESTADODOC")
public class DocumentStatus extends BaseCatalog{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESTADODOC")
    private Integer id;
}
