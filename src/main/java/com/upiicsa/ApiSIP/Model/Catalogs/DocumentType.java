package com.upiicsa.ApiSIP.Model.Catalogs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "SIP_CTIPODOC")
public class DocumentType extends BaseCatalog{

    @Id
    @Column(name = "ID_TIPODOC")
    private Integer id;
}
