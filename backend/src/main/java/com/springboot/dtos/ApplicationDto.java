package com.springboot.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDto {
    private long id;
    private String salesAgentFirstName;
    private String salesAgentLastName;
    private String salesAgentEmail;
    private String accountType;
    private Date createdAt;
    private String applicationStatus;
    private String businessCategory;
    private Date updatedAt;
}
