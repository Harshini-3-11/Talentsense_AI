package com.talentsense.interview.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleInterviewRequest {

    @NotBlank(message = "Application ID is required")
    private String applicationId;

    @NotBlank(message = "Interview type is required")
    private String interviewType;

    private LocalDateTime scheduledAt;
    private Integer durationMinutes;
}
