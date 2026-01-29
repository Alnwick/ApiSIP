package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Dto.CedulaDto;
import com.upiicsa.ApiSIP.Service.Document.CedulaService;
import com.upiicsa.ApiSIP.Utils.AuthHelper;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("cedula/")
public class CedulaController {

    private CedulaService cedulaService;

    public CedulaController(CedulaService cedulaService) {
        this.cedulaService = cedulaService;
    }

    @GetMapping("/get-data")
    @PreAuthorize("hasAnyRole('ALUMNO')")
    public ResponseEntity<CedulaDto> getCedulaData() {
        CedulaDto data = cedulaService.getAllData(AuthHelper.getAuthenticatedUserId());

        return ResponseEntity.ok(data);
    }

    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('ALUMNO')")
    public ResponseEntity<String> generateCedula(@RequestBody @Valid CedulaDto cedulaDto) {
        Integer studentId = AuthHelper.getAuthenticatedUserId();

        return ResponseEntity.ok(cedulaService.generateCedula(studentId, cedulaDto));
    }

    @GetMapping("/view-pdf")
    @PreAuthorize("hasAnyRole('ALUMNO')")
    public ResponseEntity<Resource>  viewCedulaPdf(){
        Integer studentId = AuthHelper.getAuthenticatedUserId();

        return cedulaService.getPdfResponseEntity(studentId);
    }
}
