package com.upiicsa.ApiSIP.Model.Catalogs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SIP_PLAN_EST")
public class Syllabus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PLAN")
    public Integer id;

    @Column(name = "CODIGO")
    public String codigo;
}
