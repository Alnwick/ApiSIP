package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.Catalogs.Carrera;
import com.upiicsa.ApiSIP.Model.Catalogs.Escuela;
import com.upiicsa.ApiSIP.Model.Catalogs.PlanEst;
import com.upiicsa.ApiSIP.Model.OfertaAca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query("SELECT DISTINCT o.escuela FROM OfertaAca o")
    List<Escuela> findAllEscuelasDisponibles();

    @Query("SELECT DISTINCT o.carrera FROM OfertaAca o WHERE o.escuela.id = :idEscuela")
    List<Carrera> findCarrerasByEscuela(@Param("idEscuela") Integer idEscuela);

    @Query("SELECT DISTINCT o.planEst FROM OfertaAca o WHERE o.escuela.id = :idEscuela AND o.carrera.id = :idCarrera")
    List<PlanEst> findPlanesByEscuelaAndCarrera(@Param("idEscuela") Integer idEscuela, @Param("idCarrera") Integer idCarrera);
}
