package com.upiicsa.ApiSIP.Model.Support;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentProcessId implements Serializable {

    @Column(name = "ID_TIPODOC")
    private Integer idTypeDocument;

    @Column(name = "ID_ESTPROCESO")
    private Integer idStateProcess;
}
