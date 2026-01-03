package com.upiicsa.ApiSIP.Model;

import com.upiicsa.ApiSIP.Model.Catalogs.DocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SIP_DOCUMENTO")
public class Document {

    @Id
    @Column(name = "ID_DOCUMENTO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_PROCESO")
    private StudentProcess studentProcess;

    @Column(name = "FECHA_CARGA")
    private LocalDateTime UploadDate;

    @Column(name = "URL")
    private String URL;

    @Column(name = "FECHA_BAJA")
    private LocalDateTime cancellationDate;

    @ManyToOne
    @JoinColumn(name = "ID_TIPODOC")
    private DocumentType documentType;
}
