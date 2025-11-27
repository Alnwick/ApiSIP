package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.Catalogs.Career;
import com.upiicsa.ApiSIP.Model.Catalogs.School;
import com.upiicsa.ApiSIP.Model.Catalogs.Syllabus;
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
    List<School> findAllEscuelasDisponibles();

    @Query("SELECT DISTINCT o.carrera FROM OfertaAca o WHERE o.escuela.id = :idEscuela")
    List<Career> findCarrerasByEscuela(@Param("idEscuela") Integer idEscuela);

    @Query("SELECT DISTINCT o.planEst FROM OfertaAca o WHERE o.escuela.id = :idEscuela AND o.carrera.id = :idCarrera")
    List<Syllabus> findPlanesByEscuelaAndCarrera(@Param("idEscuela") Integer idEscuela, @Param("idCarrera") Integer idCarrera);
}
