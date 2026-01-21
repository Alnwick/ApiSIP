package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.Catalogs.CareerDto;
import com.upiicsa.ApiSIP.Dto.Catalogs.SchoolDto;
import com.upiicsa.ApiSIP.Dto.Catalogs.SemesterDto;
import com.upiicsa.ApiSIP.Dto.Catalogs.SyllabusDto;
import com.upiicsa.ApiSIP.Dto.Student.StudentRegistrationDto;
import com.upiicsa.ApiSIP.Exception.ResourceNotFoundException;
import com.upiicsa.ApiSIP.Model.Catalogs.Career;
import com.upiicsa.ApiSIP.Model.Catalogs.School;
import com.upiicsa.ApiSIP.Model.Catalogs.Semester;
import com.upiicsa.ApiSIP.Model.Catalogs.Status;
import com.upiicsa.ApiSIP.Model.Offer;
import com.upiicsa.ApiSIP.Model.UserType;
import com.upiicsa.ApiSIP.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogsService {

    private SemesterRepository semesterRepository;

    private OfferRepository offerRepository;

    private SchoolRepository schoolRepository;

    private CareerRepository careerRepository;

    private UserTypeRepository typeRepository;

    private StatusRepository statusRepository;

    public CatalogsService(SemesterRepository semesterRepository, OfferRepository offerRepository,
                           SchoolRepository schoolRepository, CareerRepository careerRepository,
                           UserTypeRepository typeRepository, StatusRepository statusRepository) {
        this.semesterRepository = semesterRepository;
        this.offerRepository = offerRepository;
        this.schoolRepository = schoolRepository;
        this.careerRepository = careerRepository;
        this.typeRepository = typeRepository;
        this.statusRepository = statusRepository;
    }

    //School
    public List<SchoolDto> getSchools() {
        return offerRepository.findAllSchools().stream()
                .map(school -> new SchoolDto(school))
                .toList();
    }
    //Career
    public List<CareerDto> getCareers(String acronym) {
        School schoolOpt = schoolRepository.findSchoolByAcronym(acronym)
                .orElseThrow(()-> new EntityNotFoundException("School with acronym " + acronym + " not found"));

        return offerRepository.findCareersBySchool(schoolOpt.getId()).stream()
                .map(career -> new CareerDto(career))
                .toList();
    }
    //Syllabus
    public List<SyllabusDto> getSyllabuses(String schoolAcronym, String careerAcronym) {
        School schoolOpt = schoolRepository.findSchoolByAcronym(schoolAcronym)
                .orElseThrow(()-> new EntityNotFoundException("School with acronym " + schoolAcronym + " not found"));
        Career careerOpt = careerRepository.findCareerByAcronym(careerAcronym)
                .orElseThrow(() -> new EntityNotFoundException("Career with acronym" + careerAcronym + " not found"));

        return offerRepository.findSyllabusBySchoolAndCareer(schoolOpt.getId(), careerOpt.getId()).stream()
                .map(syllabus -> new SyllabusDto(syllabus))
                .toList();
    }
    //Offer
    public Offer getOffer(StudentRegistrationDto  registrationDto) {
        return offerRepository.findByCompositeKeys(
                        registrationDto.schoolName(), registrationDto.acronymCareer(), registrationDto.syllabusCode())
                .orElseThrow(() -> new ResourceNotFoundException("OfertaAca not found"));

    }
    //Semester
    public Semester getSemester(StudentRegistrationDto registrationDto) {
        Semester semester = null;

        if(!registrationDto.graduated()) {
            semester = semesterRepository.findByDescription(registrationDto.semester())
                    .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));
        }
        return semester;
    }

    public List<SemesterDto> getSemesters() {
        return semesterRepository.findAll().stream()
                .map(sem -> new SemesterDto(sem))
                .toList();
    }
    //UserType
    public UserType getType(String type) {
        return typeRepository.findByDescription(type)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Usuario not found"));
    }
    //Status
    public Status getStatus(String status) {
        return statusRepository.findByDescription(status)
                .orElseThrow(() -> new ResourceNotFoundException("Estatus not found"));
    }
}
