package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.OfertaAca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfertaAcaRepository extends JpaRepository<OfertaAca, Integer> {

    @Query("SELECT o FROM OfertaAca o " +
            "WHERE o.escuela.nombre = :nombreEscuela " +
            "AND o.carrera.siglas = :nombreCarrera " +
            "AND o.planEst.codigo = :codigoPlan")
    Optional<OfertaAca> findByCompositeKeys(
            @Param("nombreEscuela") String escuela,
            @Param("nombreCarrera") String carrera,
            @Param("codigoPlan") String plan
    );
}
