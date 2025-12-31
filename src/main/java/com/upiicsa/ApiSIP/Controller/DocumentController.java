package com.upiicsa.ApiSIP.Controller;

import com.upiicsa.ApiSIP.Service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @RequestMapping("/upload")
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ResponseEntity<Void> uploadDocument(MultipartFile file, String type) throws IOException {
        documentService.saveDoc(file, type);

        return ResponseEntity.ok().build();
    }
}
