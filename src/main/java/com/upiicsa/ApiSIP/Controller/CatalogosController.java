package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Model.Catalogs.Carrera;
import com.upiicsa.ApiSIP.Model.Catalogs.Escuela;
import com.upiicsa.ApiSIP.Model.Catalogs.PlanEst;
import com.upiicsa.ApiSIP.Model.Catalogs.Semestre;
import com.upiicsa.ApiSIP.Repository.OfertaAcaRepository;
import com.upiicsa.ApiSIP.Repository.SemestreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/catalogos")
public class CatalogosController {

    @Autowired
    private OfertaAcaRepository ofertaAcaRepository;

    @Autowired
    private SemestreRepository semestreRepository;

    @GetMapping("/escuelas")
    public ResponseEntity<List<Escuela>> getEscuelas() {
        return ResponseEntity.ok(ofertaAcaRepository.findAllEscuelasDisponibles());
    }

    @GetMapping("/carreras")
    public ResponseEntity<List<Carrera>> getCarreras(@RequestParam Integer escuelaId) {
        return ResponseEntity.ok(ofertaAcaRepository.findCarrerasByEscuela(escuelaId));
    }

    @GetMapping("/planes")
    public ResponseEntity<List<PlanEst>> getPlanes(@RequestParam Integer escuelaId, @RequestParam Integer carreraId) {
        return ResponseEntity.ok(ofertaAcaRepository.findPlanesByEscuelaAndCarrera(escuelaId, carreraId));
    }

    @GetMapping("/semestres")
    public ResponseEntity<List<Semestre>> getSemestres() {
        return ResponseEntity.ok(semestreRepository.findAll());
    }
}
