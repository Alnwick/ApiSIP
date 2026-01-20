package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Model.Catalogs.ProcessState;
import com.upiicsa.ApiSIP.Model.Enum.StateProcessEnum;
import com.upiicsa.ApiSIP.Model.History;
import com.upiicsa.ApiSIP.Model.StudentProcess;
import com.upiicsa.ApiSIP.Model.UserSIP;
import com.upiicsa.ApiSIP.Repository.HistoryRepository;
import com.upiicsa.ApiSIP.Repository.ProcessStateRepository;
import com.upiicsa.ApiSIP.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProcessStateRepository stateRepository;

    @Transactional
    public void saveHistory(StudentProcess process, StateProcessEnum oldState, StateProcessEnum newState) {
        ProcessState newStateProcess = stateRepository.findByDescription(newState.getName())
                .orElseThrow(()-> new EntityNotFoundException("State not found"));
        ProcessState oldStateProcess = stateRepository.findByDescription(oldState.getName())
                .orElseThrow(()-> new EntityNotFoundException("State not found"));

        History newHistory = History.builder()
                .process(process)
                .user(getDefaultUser())
                .updateDate(LocalDateTime.now())
                .newState(newStateProcess)
                .oldState(oldStateProcess)
                .build();

        historyRepository.save(newHistory);
    }

    private UserSIP getDefaultUser(){
        UserSIP user = userRepository.findById(3)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user;
    }
}
