package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.Student.StudentRegistrationDto;
import com.upiicsa.ApiSIP.Exception.ResourceNotFoundException;
import com.upiicsa.ApiSIP.Model.Catalogs.Semester;
import com.upiicsa.ApiSIP.Model.Catalogs.Status;
import com.upiicsa.ApiSIP.Model.Offer;
import com.upiicsa.ApiSIP.Model.UserType;
import com.upiicsa.ApiSIP.Repository.OfferRepository;
import com.upiicsa.ApiSIP.Repository.SemesterRepository;
import com.upiicsa.ApiSIP.Repository.StatusRepository;
import com.upiicsa.ApiSIP.Repository.TypeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataForStudentService {

    @Autowired
    private TypeUserRepository typeUserRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private OfferRepository offerRepository;

    public UserType getType(String type) {
        return typeUserRepository.findByDescription(type)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de Usuario not found"));
    }

    public Status getStatus(String status) {
        return statusRepository.findByDescription(status)
                .orElseThrow(() -> new ResourceNotFoundException("Estatus not found"));
    }

    public Semester getSemester(StudentRegistrationDto  registrationDto) {
        Semester semester = null;

        if(!registrationDto.graduated()) {
            semester = semesterRepository.findByDescription(registrationDto.semester())
                    .orElseThrow(() -> new ResourceNotFoundException("Semester not found"));
        }
        return semester;
    }

    public Offer getOffer(StudentRegistrationDto  registrationDto) {
        return offerRepository.findByCompositeKeys(
                        registrationDto.schoolName(), registrationDto.acronymCareer(), registrationDto.syllabusCode())
                .orElseThrow(() -> new ResourceNotFoundException("OfertaAca not found"));

    }
}
