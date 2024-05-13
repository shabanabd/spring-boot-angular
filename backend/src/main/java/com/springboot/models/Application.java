package com.springboot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "application")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_application_id")
    private long id;
    @Column(name = "sales_agent_first_name")
    private String salesAgentFirstName;
    @Column(name = "sales_agent_last_name")
    private String salesAgentLastName;
    @Column(name = "sales_agent_email")
    private String salesAgentEmail;
    @Column(name = "account_type")
    private String accountType;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "application_status")
    private String applicationStatus;
    @Column(name = "business_category")
    private String businessCategory;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
