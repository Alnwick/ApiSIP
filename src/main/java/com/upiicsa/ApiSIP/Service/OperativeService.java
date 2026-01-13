package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.DashboardStatsDto;
import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Repository.StudentProcessRepository;
import com.upiicsa.ApiSIP.Repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OperativeService {

    private StudentRepository studentRepository;

    private StudentProcessRepository processRepository;

    public DashboardStatsDto getStats(String careerAcronym){
        List<Student> students;

        if(careerAcronym.equals("all")){
            students = studentRepository.findAll();
        }
        students = studentRepository.findAllByCareerAcronym(careerAcronym);

        Map<String, Long> counts = students.stream()
                .map(student -> processRepository.findByStudentId(student.getId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        process -> process.getProcessState().getDescription(),
                        Collectors.counting()
                ));

        return new DashboardStatsDto(
                students.size(),
                counts.getOrDefault("Registrado", 0L).intValue(),
                counts.getOrDefault("Doc Inicial", 0L).intValue(),
                counts.getOrDefault("Carta Aceptacion", 0L).intValue(),
                counts.getOrDefault("Doc Final", 0L).intValue()
        );
    }

}
