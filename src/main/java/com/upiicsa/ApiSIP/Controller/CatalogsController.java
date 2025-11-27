package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Model.Catalogs.Career;
import com.upiicsa.ApiSIP.Model.Catalogs.School;
import com.upiicsa.ApiSIP.Model.Catalogs.Syllabus;
import com.upiicsa.ApiSIP.Model.Catalogs.Semester;
import com.upiicsa.ApiSIP.Repository.OfferRepository;
import com.upiicsa.ApiSIP.Repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
public class CatalogsController {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @GetMapping("/schools")
    public ResponseEntity<List<School>> getSchools() {
        return ResponseEntity.ok(offerRepository.findAllSchools());
    }

    @GetMapping("/careers")
    public ResponseEntity<List<Career>> getCareers(@RequestParam Integer careerId) {
        return ResponseEntity.ok(offerRepository.findCareersBySchool(careerId));
    }

    @GetMapping("/syllabus")
    public ResponseEntity<List<Syllabus>> getSyllabus(@RequestParam Integer schoolId, @RequestParam Integer careerId) {
        return ResponseEntity.ok(offerRepository.findSyllabusBySchoolAndCareer(schoolId, careerId));
    }

    @GetMapping("/semesters")
    public ResponseEntity<List<Semester>> getSemesters() {
        return ResponseEntity.ok(semesterRepository.findAll());
    }
}
