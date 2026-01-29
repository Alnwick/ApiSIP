package com.upiicsa.ApiSIP.Model.Document_Process;

import jakarta.persistence.Column;
import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDocumentId implements Serializable {

    @Column(name = "ID_OPERATIVO")
    private Integer idOperative;

    @Column(name = "ID_DOCUMENTO")
    private Integer idDocument;
}
