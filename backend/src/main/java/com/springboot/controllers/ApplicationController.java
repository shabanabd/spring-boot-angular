package com.springboot.controllers;

import com.springboot.dtos.ApplicationDto;
import com.springboot.dtos.StatisticsDto;
import com.springboot.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ApplicationController {
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/applications")
    public ResponseEntity<Page<ApplicationDto>> getApplications(@SortDefault(sort = "id") @PageableDefault(size = 5) final Pageable pageable,
                                                                   @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
                                                                   @RequestParam(value = "createdDate", required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") Date createdDate,
                                                                   @RequestParam(value = "updatedDate", required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") Date updatedDate
                                                                   ) throws Exception {
        return new ResponseEntity<>(applicationService.getAllApplications(searchText, createdDate, updatedDate, pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/applications/export")
    public ResponseEntity<byte[]> exportApplications(@SortDefault(sort = "id") @PageableDefault(size = 5) final Pageable pageable,
                                                                   @RequestParam(value = "searchText", required = false, defaultValue = "") String searchText,
                                                                   @RequestParam(value = "createdDate", required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") Date createdDate,
                                                                   @RequestParam(value = "updatedDate", required = false, defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") Date updatedDate
    ) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Applications.csv");

        byte[] csvBytes = applicationService.exportApplicationsToCSV(searchText, createdDate, updatedDate, pageable);

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/applications")
    public ResponseEntity<Long> saveApplication(@RequestBody ApplicationDto applicationDto) throws Exception {
        long id = applicationService.saveApplication(applicationDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PutMapping("/applications")
    public ResponseEntity<Long> updateApplication(@RequestBody ApplicationDto applicationDto) throws Exception {
        long id = applicationService.updateApplication(applicationDto);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("/applications/{appId}")
    public ResponseEntity deleteApplication(@PathVariable Long appId) throws Exception {
        applicationService.deleteApplication(appId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/applications/statistics")
    public ResponseEntity<StatisticsDto> getApplicationStatistics() throws Exception {
        return new ResponseEntity<>(applicationService.getApplicationStatistics(), HttpStatus.OK);
    }
}
