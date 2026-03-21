package com.upiicsa.ApiSIP.Service;

import com.upiicsa.ApiSIP.Model.Catalogs.ProcessStatus;
import com.upiicsa.ApiSIP.Model.History;
import com.upiicsa.ApiSIP.Model.Document_Process.StudentProcess;
import com.upiicsa.ApiSIP.Model.UserSIP;
import com.upiicsa.ApiSIP.Repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryService {

    @Value("${default.user}")
    private Integer defaultUser;
    private HistoryRepository historyRepository;
    private UserService userService;

    public HistoryService(HistoryRepository historyRepository, UserService userService) {
        this.historyRepository = historyRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<History> getHistoriesByProcess(StudentProcess process){
        return historyRepository.findByProcessOrderByUpdateDateAsc(process);
    }

    @Transactional
    public void saveHistory(StudentProcess process, ProcessStatus oldStatus, ProcessStatus newStatus) {

        History newHistory = History.builder()
                .process(process)
                .user(getDefaultUser())
                .updateDate(LocalDateTime.now())
                .newState(oldStatus)
                .oldState(newStatus)
                .build();

        historyRepository.save(newHistory);
    }

    private UserSIP getDefaultUser(){
        UserSIP user = userService.getUserById(defaultUser)
                .orElse(null);
        return user;
    }
}
