package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.Catalogs.CareerDto;
import com.upiicsa.ApiSIP.Dto.Catalogs.SchoolDto;
import com.upiicsa.ApiSIP.Dto.Catalogs.SemesterDto;
import com.upiicsa.ApiSIP.Dto.Catalogs.SyllabusDto;
import com.upiicsa.ApiSIP.Model.Catalogs.Career;
import com.upiicsa.ApiSIP.Model.Catalogs.School;
import com.upiicsa.ApiSIP.Repository.CareerRepository;
import com.upiicsa.ApiSIP.Repository.OfferRepository;
import com.upiicsa.ApiSIP.Repository.SchoolRepository;
import com.upiicsa.ApiSIP.Repository.SemesterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogsService {

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private CareerRepository careerRepository;

    public List<SchoolDto> getSchools() {
        return offerRepository.findAllSchools().stream()
                .map(school -> new SchoolDto(school))
                .toList();
    }

    public List<CareerDto> getCareers(String acronym) {
        School schoolOpt = schoolRepository.findSchoolByAcronym(acronym)
                .orElseThrow(()-> new EntityNotFoundException("School with acronym " + acronym + " not found"));

        return offerRepository.findCareersBySchool(schoolOpt.getId()).stream()
                .map(career -> new CareerDto(career))
                .toList();
    }

    public List<SyllabusDto> getSyllabuses(String schoolAcronym, String careerAcronym) {
        School schoolOpt = schoolRepository.findSchoolByAcronym(schoolAcronym)
                .orElseThrow(()-> new EntityNotFoundException("School with acronym " + schoolAcronym + " not found"));
        Career careerOpt = careerRepository.findCareerByAcronym(careerAcronym)
                .orElseThrow(() -> new EntityNotFoundException("Career with acronym" + careerAcronym + " not found"));

        return offerRepository.findSyllabusBySchoolAndCareer(schoolOpt.getId(), careerOpt.getId()).stream()
                .map(syllabus -> new SyllabusDto(syllabus))
                .toList();
    }
    public List<SemesterDto> getSemesters() {
        return semesterRepository.findAll().stream()
                .map(sem -> new SemesterDto(sem))
                .toList();
    }
}
