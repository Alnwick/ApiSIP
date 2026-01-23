package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.AddressDto;
import com.upiicsa.ApiSIP.Dto.CedulaDto;
import com.upiicsa.ApiSIP.Dto.CompanyDto;
import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Model.StudentProcess;
import com.upiicsa.ApiSIP.Repository.StudentProcessRepository;
import com.upiicsa.ApiSIP.Repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CedulaService {

    private StudentRepository studentRepository;
    private StudentProcessRepository processRepository;

    public CedulaService(StudentRepository sRepository, StudentProcessRepository pRepository) {
        this.studentRepository = sRepository;
        this.processRepository = pRepository;
    }

    public CedulaDto getAllData(Integer studentId){
        Student student =  studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        AddressDto studentAddress = new AddressDto(student.getAddress());

        StudentProcess studentProcess = processRepository.findByStudentId(student.getId())
                .orElseThrow(() -> new EntityNotFoundException("StudentProcess not found"));

        CompanyDto company = new CompanyDto(studentProcess.getCompany());
        AddressDto companyAddress =  new AddressDto(studentProcess.getCompany().getAddress());

        return new CedulaDto(studentAddress, company, companyAddress);
    }
}
