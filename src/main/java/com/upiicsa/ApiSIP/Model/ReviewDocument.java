package com.upiicsa.ApiSIP.Model;

import com.upiicsa.ApiSIP.Model.Catalogs.DocumentStatus;
import com.upiicsa.ApiSIP.Model.Support.ReviewDocumentId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "SIP_REVISIONDOC")
public class ReviewDocument {

    @EmbeddedId
    private ReviewDocumentId id = new ReviewDocumentId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idOperative")
    @JoinColumn(name = "ID_OPERATIVO")
    private UserSIP operative;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idDocument")
    @JoinColumn(name = "ID_DOCUMENTO")
    private Document document;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ESTADODOC")
    private DocumentStatus status;

    @Column(name = "FECHA_REVISION")
    private LocalDateTime reviewDate;

    @Column(name = "COMENTARIO", length = 150)
    private String comment;

    public ReviewDocument(UserSIP operative, Document document, DocumentStatus status, String comment) {
        this.operative = operative;
        this.document = document;
        this.status = status;
        this.comment = comment;
    }

    public ReviewDocument() {

    }
}