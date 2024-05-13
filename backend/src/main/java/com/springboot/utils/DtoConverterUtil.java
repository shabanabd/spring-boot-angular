package com.springboot.utils;

import com.springboot.dtos.ApplicationDto;
import com.springboot.models.Application;

public class DtoConverterUtil {

    public static ApplicationDto toApplicationDto(Application application) {
        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setId(application.getId());
        applicationDto.setApplicationStatus(application.getApplicationStatus());
        applicationDto.setAccountType(application.getAccountType());
        applicationDto.setCreatedAt(application.getCreatedAt());
        applicationDto.setUpdatedAt(application.getUpdatedAt());
        applicationDto.setBusinessCategory(application.getBusinessCategory());
        applicationDto.setSalesAgentEmail(application.getSalesAgentEmail());
        applicationDto.setSalesAgentFirstName(application.getSalesAgentFirstName());
        applicationDto.setSalesAgentLastName(application.getSalesAgentLastName());
        return applicationDto;
    }
}
