package com.talentsense.job.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {

    private String id;
    private String organizationId;
    private String companyName;
    private String createdByUserId;
    private String title;
    private String department;
    private String description;
    private String location;
    private String remoteType;
    private String employmentType;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String currency;
    private Integer experienceMin;
    private Integer experienceMax;
    private String seniority;
    private String industry;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
