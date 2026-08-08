package com.talentsense.job.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobRequest {

    @NotBlank(message = "Job title is required")
    @Size(max = 150, message = "Job title cannot exceed 150 characters")
    private String title;

    private String department;

    @NotBlank(message = "Job description is required")
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
}
