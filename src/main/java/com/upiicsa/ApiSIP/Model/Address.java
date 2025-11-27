package com.upiicsa.ApiSIP.Model;

import com.upiicsa.ApiSIP.Model.Catalogs.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SIP_DIRECCION")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DIRECCION")
    private Integer id;

    @Column(name = "CALLE", length = 100)
    private String street;

    @Column(name = "NUM_EXT", length = 5)
    private String outNumber;

    @Column(name = "NUM_INT", length = 5)
    private String intNumber;

    @Column(name = "COLONIA", length = 100)
    private String colony;

    @Column(name = "CODIGOP", length = 10)
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "ID_EDO")
    private State state;
}
