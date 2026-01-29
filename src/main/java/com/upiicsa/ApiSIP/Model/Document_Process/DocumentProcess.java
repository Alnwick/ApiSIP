package com.upiicsa.ApiSIP.Model.Document_Process;

import com.upiicsa.ApiSIP.Model.Catalogs.DocumentType;
import com.upiicsa.ApiSIP.Model.Catalogs.ProcessState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "SIP_CPROCESODOC")
public class DocumentProcess {

    @EmbeddedId
    private DocumentProcessId id = new DocumentProcessId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idTypeDocument")
    @JoinColumn(name = "ID_TIPODOC")
    private DocumentType documentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idStateProcess")
    @JoinColumn(name = "ID_ESTPROCESO")
    private ProcessState processState;

    @Column(name = "REQUERIDO")
    private Boolean requery;

    public DocumentProcess(DocumentType documentType,  ProcessState processState, Boolean requery) {
        this.documentType = documentType;
        this.processState = processState;
        this.requery = requery;
    }
    public DocumentProcess(){

    }
}
