package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.AddressDto;
import com.upiicsa.ApiSIP.Dto.CedulaDto;
import com.upiicsa.ApiSIP.Dto.CompanyDto;
import com.upiicsa.ApiSIP.Model.Address;
import com.upiicsa.ApiSIP.Model.Catalogs.State;
import com.upiicsa.ApiSIP.Model.Company;
import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Model.StudentProcess;
import com.upiicsa.ApiSIP.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CedulaService {

    private StudentRepository studentRepository;
    private StudentProcessRepository processRepository;
    private StateRepository stateRepository;
    private AddressRepository addressRepository;

    public CedulaService(StudentRepository sRepository, StudentProcessRepository pRepository,
                         StateRepository stateRepository, AddressRepository addressRepository) {
        this.studentRepository = sRepository;
        this.processRepository = pRepository;
        this.stateRepository = stateRepository;
        this.addressRepository = addressRepository;
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

    public String generateCedula(Integer studentId, CedulaDto cedulaDto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        if(student.getAddress() != null){
            Address studentAddress = createAddress(cedulaDto.studentAddress());

            student.setAddress(studentAddress);
            studentRepository.save(student);
        }
        StudentProcess studentProcess = processRepository.findByStudentId(student.getId())
                .orElseThrow(() -> new EntityNotFoundException("StudentProcess not found"));

        if(studentProcess.getCompany() != null){
            Address companyAddress = createAddress(cedulaDto.companyAddress());

            Company company = Company.builder()
                    .name(cedulaDto.companyInfo().name()).email(cedulaDto.companyInfo().email())
                    .sector(cedulaDto.companyInfo().sector()).phone(cedulaDto.companyInfo().phone())
                    .extension(cedulaDto.companyInfo().extension()).fax(cedulaDto.companyInfo().fax())
                    .supervisor(cedulaDto.companyInfo().supervisor())
                    .supervisorGrade(cedulaDto.companyInfo().supervisorGrade())
                    .positionSupervisor(cedulaDto.companyInfo().positionSupervisor())
                    .positionStudent(cedulaDto.companyInfo().positionStudent())
                    .address(companyAddress)
                    .build();
            addressRepository.save(companyAddress);
        }
        return "";
    }

    public Address createAddress(AddressDto addressDto) {

        State state = stateRepository.findById(addressDto.stateId())
                .orElseThrow(() -> new EntityNotFoundException("State not found"));

        Address newAddress = Address.builder()
                .street(addressDto.street())
                .number(addressDto.number())
                .neighborhood(addressDto.neighborhood())
                .zipCode(addressDto.zipCode())
                .state(state)
                .build();
        addressRepository.save(newAddress);
        return newAddress;
    }
}
