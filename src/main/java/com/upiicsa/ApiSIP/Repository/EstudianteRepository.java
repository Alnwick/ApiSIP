package com.upiicsa.ApiSIP.Repository;

import com.upiicsa.ApiSIP.Model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Alumno, Integer> {

}
