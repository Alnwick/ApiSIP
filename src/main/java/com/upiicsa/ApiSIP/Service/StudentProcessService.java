package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Repository.StudentProcessRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentProcessService {

    @Autowired
    private StudentProcessRepository processRepository;

    @Transactional
    public void setFirstState(Student student) {


    }
}
