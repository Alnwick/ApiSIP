package com.upiicsa.ApiSIP.Model;

import com.upiicsa.ApiSIP.Model.Catalogs.DocumentType;
import com.upiicsa.ApiSIP.Model.Catalogs.ProcessState;
import com.upiicsa.ApiSIP.Model.Support.DocumentProcessId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "SIP_CPROCESODOC")
public class DocumentProcess {

    @EmbeddedId
    private DocumentProcess id = new DocumentProcessId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idTypeDocument")
    @JoinColumn("ID_TIPDOC")
    private DocumentType documentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idTypeDocument")
    @JoinColumn("ID_ESTPROCESO")
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
