package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Dto.Catalogs.*;
import com.upiicsa.ApiSIP.Service.CatalogsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
public class CatalogsController {

    private CatalogsService catalogsService;

    public CatalogsController(CatalogsService catalogsService) {
        this.catalogsService = catalogsService;
    }

    @GetMapping("/schools")
    public ResponseEntity<List<SchoolDto>> getSchools() {
        return ResponseEntity.ok(catalogsService.getSchools());
    }

    @GetMapping("/careers")
    public ResponseEntity<List<CareerDto>> getCareers(@RequestParam String SchoolName) {
        return ResponseEntity.ok(catalogsService.getCareers(SchoolName));
    }

    @GetMapping("/syllabus")
    public ResponseEntity<List<SyllabusDto>> getSyllabuses(@RequestParam String schoolAcronym, @RequestParam String careerAcronym) {
        return ResponseEntity.ok(catalogsService.getSyllabuses(schoolAcronym, careerAcronym));
    }

    @GetMapping("/semesters")
    public ResponseEntity<List<SemesterDto>> getSemesters() {
        return ResponseEntity.ok(catalogsService.getSemesters());
    }

    @GetMapping("/states")
    public ResponseEntity<List<StateDto>> getStates(){
        return ResponseEntity.ok(catalogsService.getStates());
    }
}
