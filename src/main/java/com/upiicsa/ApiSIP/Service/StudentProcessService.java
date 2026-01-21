package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Dto.ProcessProgressDto;
import com.upiicsa.ApiSIP.Exception.ResourceNotFoundException;
import com.upiicsa.ApiSIP.Model.Catalogs.ProcessState;
import com.upiicsa.ApiSIP.Model.Enum.StateProcessEnum;
import com.upiicsa.ApiSIP.Model.History;
import com.upiicsa.ApiSIP.Model.Student;
import com.upiicsa.ApiSIP.Model.StudentProcess;
import com.upiicsa.ApiSIP.Repository.ProcessStateRepository;
import com.upiicsa.ApiSIP.Repository.StudentProcessRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentProcessService {

    private StudentProcessRepository processRepository;

    private ProcessStateRepository processStateRepository;

    private HistoryService historyService;

    public StudentProcessService(StudentProcessRepository processRepository, ProcessStateRepository processStateRepository,
                                 HistoryService historyService) {
        this.processRepository = processRepository;
        this.processStateRepository = processStateRepository;
        this.historyService = historyService;
    }

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

    @Transactional
    public void updateProcessStatus(Integer processId, StateProcessEnum nextProcess) {
        StudentProcess process = processRepository.findById(processId)
                .orElseThrow(() -> new EntityNotFoundException("Process not found"));

        StateProcessEnum currentState = StateProcessEnum.fromId(process.getProcessState().getId());

        boolean isValid = currentState.getNextId() == nextProcess.getId() ||
                nextProcess == StateProcessEnum.CANCELLATION;

        if (!isValid) {
            throw new IllegalStateException("Transición no permitida");
        }

        ProcessState newState = processStateRepository.findById(nextProcess.getId())
                .orElseThrow(() -> new EntityNotFoundException("Estado no encontrado"));
        process.setProcessState(newState);

        processRepository.save(process);
        historyService.saveHistory(process, currentState, nextProcess);
    }

    public List<ProcessProgressDto> getProcessHistory(Integer userId) {
        StudentProcess process = processRepository.findByStudentId(userId)
                .filter(StudentProcess::getActive)
                .orElseThrow(() -> new RuntimeException("Proceso no encontrado"));

        List<History> history = historyService.getHistoriesByProcess(process);

        String[] stages = {
                "Registrado", "Documentación de inicio", "Carta de aceptación",
                "Finalización de informes", "Documentación de término", "Liberación"
        };

        List<ProcessProgressDto> progress = new ArrayList<>();

        for (int i = 0; i < stages.length; i++) {
            int stageId = i + 1;

            Optional<History> entry = history.stream()
                    .filter(h -> h.getProcess().getId() == stageId)
                    .findFirst();

            String dateStr = entry.map(value -> value.getUpdateDate().toLocalDate().toString())
                    .orElse("-");
            boolean current = process.getProcessState().getId() == stageId;

            progress.add(new ProcessProgressDto(stages[i], dateStr, current));
        }
        return progress;
    }

    public StudentProcess getByStudentId(Integer userId) {
        return processRepository.findByStudentId(userId)
                .orElseThrow(()->new IllegalArgumentException("Process not found"));
    }
}
