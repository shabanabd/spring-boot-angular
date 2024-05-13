package com.springboot.service;

import com.springboot.dtos.ApplicationDto;
import com.springboot.dtos.StatisticsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface ApplicationService {

    Page<ApplicationDto> getAllApplications(String searchText, Date from, Date to, final Pageable pageable) throws Exception;
    byte[] exportApplicationsToCSV(String searchText, Date from, Date to, final Pageable pageable) throws Exception;

    long saveApplication(ApplicationDto applicationDto) throws Exception;
    long updateApplication(ApplicationDto applicationDto) throws Exception;
    void deleteApplication(long id) throws Exception;
    StatisticsDto getApplicationStatistics() throws Exception;
}
