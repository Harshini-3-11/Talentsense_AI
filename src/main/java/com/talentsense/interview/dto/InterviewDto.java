package com.talentsense.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDto {

    private String id;
    private String applicationId;
    private String candidateId;
    private String candidateName;
    private String jobId;
    private String jobTitle;
    private String interviewType;
    private String status;
    private LocalDateTime scheduledAt;
    private Integer durationMinutes;
    private String feedbackSummary;
    private Integer overallScore;
}
