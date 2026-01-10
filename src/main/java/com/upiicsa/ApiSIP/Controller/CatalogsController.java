package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Dto.Catalogs.CareerDto;
import com.upiicsa.ApiSIP.Dto.Catalogs.SchoolDto;
import com.upiicsa.ApiSIP.Dto.Catalogs.SemesterDto;
import com.upiicsa.ApiSIP.Dto.Catalogs.SyllabusDto;
import com.upiicsa.ApiSIP.Service.CatalogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
public class CatalogsController {

    @Autowired
    private CatalogsService catalogsService;

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
}
