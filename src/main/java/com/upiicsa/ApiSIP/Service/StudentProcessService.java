package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Exception.ResourceNotFoundException;
import com.upiicsa.ApiSIP.Model.Enum.StateProcessEnum;
import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Model.StudentProcess;
import com.upiicsa.ApiSIP.Repository.ProcessStateRepository;
import com.upiicsa.ApiSIP.Repository.StudentProcessRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StudentProcessService {

    @Autowired
    private StudentProcessRepository processRepository;

    @Autowired
    private ProcessStateRepository  processStateRepository;

    @Transactional
    public void setFirstState(Student student) {
        var state = processStateRepository.findByDescription(StateProcessEnum.REGISTERED.getName())
                        .orElseThrow(()-> new ResourceNotFoundException("State not found"));

        StudentProcess firstProcess = StudentProcess.builder()
                .StartDate(LocalDateTime.now())
                .Active(true)
                .student(student)
                .processState(state)
                .observations("")
                .build();

        processRepository.save(firstProcess);
    }
}
