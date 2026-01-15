package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Model.Catalogs.DocumentType;
import com.upiicsa.ApiSIP.Model.StudentProcess;
import com.upiicsa.ApiSIP.Repository.DocumentProcessRepository;
import com.upiicsa.ApiSIP.Repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentTypeService {

    @Autowired
    private DocumentTypeRepository typeRepository;

    @Autowired
    private DocumentProcessRepository docProcessRepository;

    public List<DocumentType> getRequiredTypes(StudentProcess process){

        return docProcessRepository.findDocumentTypesByProcessState(process.getProcessState());
    }

    public DocumentType getByDescription(String typeName){
        return typeRepository.findByDescription(typeName).orElse(null);
    }

}
