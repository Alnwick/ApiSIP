package com.upiicsa.ApiSIP.Model;

import com.upiicsa.ApiSIP.Model.Catalogs.Carrera;
import com.upiicsa.ApiSIP.Model.Catalogs.Escuela;
import com.upiicsa.ApiSIP.Model.Catalogs.PlanEst;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIP_OFERTA_ACA")
public class OfertaAca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_OFERTA")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_ESCUELA")
    private Escuela escuela;

    @ManyToOne
    @JoinColumn(name = "ID_CARRERA")
    private Carrera carrera;

    @ManyToOne
    @JoinColumn(name = "ID_PLAN")
    private PlanEst planEst;
}
