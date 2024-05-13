package com.springboot.service;

import com.springboot.dtos.ApplicationDto;
import com.springboot.dtos.StatisticsDto;
import com.springboot.models.Application;
import com.springboot.repositories.ApplicationRepository;
import com.springboot.utils.DateUtil;
import com.springboot.utils.DtoConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Page<ApplicationDto> getAllApplications(String searchText, Date from, Date to, final Pageable pageable) throws Exception{
        Page<Application> page = null;

        if ((searchText != null && !searchText.isEmpty()) || (from != null && to != null)) {
            page = applicationRepository.findAll(getFilterSpecification(searchText, from, to), pageable);
        } else {
            page = applicationRepository.findAll(pageable);
        }

        return new PageImpl<ApplicationDto>(page.getContent()
                .stream()
                .map(app -> DtoConverterUtil.toApplicationDto(app))
                .collect(Collectors.toList()),
                pageable, page.getTotalElements());
    }

    @Override
    public byte[] exportApplicationsToCSV(String searchText, Date from, Date to, final Pageable pageable) throws Exception {
        final String CSV_HEADER = "Application ID,Sales Agent Id,Account Type,Created on,Application Status,Business Category,Last Action On\n";
        List<Application> applicationList = null;
        if ((searchText != null && !searchText.isEmpty()) || (from != null && to != null)) {
            applicationList = applicationRepository.findAll(getFilterSpecification(searchText, from, to), pageable.getSort());
        } else {
            applicationList = applicationRepository.findAll(pageable.getSort());
        }

        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);

        for (Application app : applicationList) {
            csvContent.append(app.getId()).append(",")
                    .append(app.getSalesAgentEmail()).append(",")
                    .append(app.getAccountType()).append(",")
                    .append(dateFormatter.format(app.getCreatedAt())).append(",")
                    .append(app.getApplicationStatus()).append(",")
                    .append(app.getBusinessCategory()).append(",")
                    .append(dateFormatter.format(app.getUpdatedAt())).append("\n");
        }

        return csvContent.toString().getBytes();
    }

    private Specification<Application> getFilterSpecification(String searchText, Date from, Date to) {
        Specification<Application> filter = null;
        if (searchText != null && !searchText.isEmpty()) {
            filter = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("salesAgentFirstName"), "%" + searchText + "%"));
            filter = filter.or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("salesAgentLastName"), "%" + searchText + "%"))
                    .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("salesAgentEmail"), "%" + searchText + "%"))
                    .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("accountType"), "%" + searchText + "%"))
                    .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("applicationStatus"), "%" + searchText + "%"))
                    .or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("businessCategory"), "%" + searchText + "%"));
        }
        if (from != null && to != null) {
            final Date endDay = DateUtil.getEnd(to);
            if (filter == null) {
                filter = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("createdAt"), from, endDay));
                filter = filter.or((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("updatedAt"), from, endDay));
            } else {
                filter = filter.and(Specification.anyOf((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("createdAt"), from, endDay),
                        (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("updatedAt"), from, endDay)));
            }
        }
        return filter;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public long saveApplication(ApplicationDto applicationDto) throws Exception {
        Application application = new Application();
        application.setApplicationStatus(applicationDto.getApplicationStatus());
        application.setAccountType(applicationDto.getAccountType());
        application.setBusinessCategory(applicationDto.getBusinessCategory());
        application.setSalesAgentEmail(applicationDto.getSalesAgentEmail());
        application.setSalesAgentFirstName(applicationDto.getSalesAgentFirstName());
        application.setSalesAgentLastName(applicationDto.getSalesAgentLastName());
        application.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        application.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return applicationRepository.save(application).getId();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public long updateApplication(ApplicationDto applicationDto) throws Exception {
        Application application = applicationRepository.findById(applicationDto.getId()).get();
        application.setApplicationStatus(applicationDto.getApplicationStatus());
        application.setAccountType(applicationDto.getAccountType());
        application.setBusinessCategory(applicationDto.getBusinessCategory());
        application.setSalesAgentEmail(applicationDto.getSalesAgentEmail());
        application.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return applicationRepository.save(application).getId();
    }

    @Transactional
    @Override
    public void deleteApplication(long id) throws Exception {
        applicationRepository.deleteById(id);
    }

    @Override
    public StatisticsDto getApplicationStatistics() throws Exception {
        StatisticsDto statisticsDto = new StatisticsDto();
        List<Application> applicationList = applicationRepository.findAll();
        long complexApp = 0;
        long simpleApp = 0;
        long incompleteApp = 0;
        long amlInprogressApp = 0;
        for (Application application : applicationList) {
            if ("complex".equalsIgnoreCase(application.getBusinessCategory()))
                complexApp++;
            else if ("Simple".equalsIgnoreCase(application.getBusinessCategory()))
                simpleApp++;
            if ("Incomplete Application".equalsIgnoreCase(application.getApplicationStatus()))
                incompleteApp++;
            else if ("AML In-Progress".equalsIgnoreCase(application.getApplicationStatus()))
                amlInprogressApp++;
        }
        statisticsDto.setApplicationNum(applicationList.size());
        statisticsDto.setComplexApp(complexApp);
        statisticsDto.setSimpleApp(simpleApp);
        statisticsDto.setIncompleteApp(incompleteApp);
        statisticsDto.setAmlInprogressApp(amlInprogressApp);
        return statisticsDto;
    }
}
