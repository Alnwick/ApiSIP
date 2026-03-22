package com.upiicsa.ApiSIP.Service.Document;

import com.upiicsa.ApiSIP.Dto.Data.ProcessProgressDto;
import com.upiicsa.ApiSIP.Exception.ResourceNotFoundException;
import com.upiicsa.ApiSIP.Exception.ValidationException;
import com.upiicsa.ApiSIP.Model.Catalogs.ProcessStatus;
import com.upiicsa.ApiSIP.Model.Document_Process.Document;
import com.upiicsa.ApiSIP.Model.Enum.StateProcessEnum;
import com.upiicsa.ApiSIP.Model.History;
import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Model.Document_Process.StudentProcess;
import com.upiicsa.ApiSIP.Repository.Catalogs.ProcessStatusRepository;
import com.upiicsa.ApiSIP.Repository.Document_Process.DocumentProcessRepository;
import com.upiicsa.ApiSIP.Repository.Document_Process.StudentProcessRepository;
import com.upiicsa.ApiSIP.Service.HistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentProcessService {

    private StudentProcessRepository processRepository;
    private DocumentProcessRepository docProcessRepository;
    private ProcessStatusRepository processStatusRepository;
    private HistoryService historyService;

    public StudentProcessService(StudentProcessRepository processRepository, DocumentProcessRepository docProcessRepository,
                                 ProcessStatusRepository processStatusRepository, HistoryService historyService) {
        this.processRepository = processRepository;
        this.docProcessRepository = docProcessRepository;
        this.processStatusRepository = processStatusRepository;
        this.historyService = historyService;
    }

    @Transactional(readOnly = true)
    public Optional<StudentProcess> findByStudentId(Integer studentId) {
        return processRepository.findByStudentIdAndReasonLeavingIsNull(studentId);
    }

    @Transactional(readOnly = true)
    public Optional<StudentProcess> findByEnrollment(String enrollment) {
        return processRepository.findByStudentEnrollmentAndReasonLeavingIsNull(enrollment);
    }

    @Transactional
    public void setFirstState(Student student) {
        var state = processStatusRepository.findByDescription(StateProcessEnum.REGISTERED.getName())
                        .orElseThrow(()-> new ResourceNotFoundException("State not found"));

        StudentProcess firstProcess = StudentProcess.builder()
                .startDate(LocalDateTime.now())
                .student(student)
                .processStatus(state)
                .reasonLeaving(null)
                .build();

        processRepository.save(firstProcess);
    }

    @Transactional
    public void updateStatus(StudentProcess process) {
        StateProcessEnum currentState = StateProcessEnum
                .fromName(process.getProcessStatus().getDescription());

        ProcessStatus newStatus = processStatusRepository.findByDescription(
                StateProcessEnum.fromId(currentState.getNextId()).getName())
                .orElseThrow(()-> new ResourceNotFoundException("Process status not found"));

        historyService.saveHistory(process, process.getProcessStatus(), newStatus);
        process.setProcessStatus(newStatus);
        processRepository.save(process);
    }

    @Transactional
    public void validateUpdateStatus(StudentProcess process, List<Document> docs) {
        int countedTrue = 0;
        int numberOfNeed = docProcessRepository.countByProcessStatus(process.getProcessStatus());

        for (Document doc : docs) {
            if(doc.getDocumentStatus().getDescription().equals("CORRECTO")){
                countedTrue++;
            }
        }
        if(!docs.isEmpty() && docs.size() == numberOfNeed){
            if(countedTrue == docs.size()){
                updateStatus(process);
            }else {
                throw new ValidationException("The all documents are not successful");
            }
        }
    }

    public List<ProcessProgressDto> getProcessHistory(Integer userId) {
        StudentProcess process = getByStudentId(userId);
        int currentStageId = process.getProcessStatus().getId();
        List<History> historyList = historyService.getHistoriesByProcess(process);

        List<ProcessProgressDto> progress = new ArrayList<>();

        for(StateProcessEnum state : StateProcessEnum.values()) {
            if(state == StateProcessEnum.CANCELLATION) continue;

            int stageId = state.getId();
            String stageName = state.getName();
            String date = "-";

            if(stageId == 1 && currentStageId > 1) {
                date = process.getStartDate().toLocalDate().toString();
            } else if (stageId > 1 && stageId <= currentStageId) {
                date = historyList.stream()
                        .filter(h -> h.getNewState().getId().equals(stageId))
                        .findFirst()
                        .map(h -> h.getUpdateDate().toLocalDate().toString())
                        .orElse("-");
            }
            progress.add(new ProcessProgressDto(stageName, date, stageId == currentStageId));
        }
        return progress;
    }

    public StudentProcess getByStudentId(Integer userId) {
        return processRepository.findByStudentIdAndReasonLeavingIsNull(userId)
                .orElseThrow(()->new IllegalArgumentException("Process not found"));
    }
}
