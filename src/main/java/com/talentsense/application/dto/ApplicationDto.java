package com.talentsense.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {

    private String id;
    private String jobId;
    private String jobTitle;
    private String companyName;
    private String candidateId;
    private String candidateName;
    private String candidateEmail;
    private String resumeId;
    private String coverLetter;
    private String status;
    private Integer matchScore;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
