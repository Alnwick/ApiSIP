package com.upiicsa.ApiSIP.Model;

import com.upiicsa.ApiSIP.Model.Catalogs.Career;
import com.upiicsa.ApiSIP.Model.Catalogs.School;
import com.upiicsa.ApiSIP.Model.Catalogs.Syllabus;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIP_OFERTA_ACA")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_OFERTA_ACA")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_ESCUELA")
    private School school;

    @ManyToOne
    @JoinColumn(name = "ID_CARRERA")
    private Career career;

    @ManyToOne
    @JoinColumn(name = "ID_PLAN_EST")
    private Syllabus syllabus;
}
