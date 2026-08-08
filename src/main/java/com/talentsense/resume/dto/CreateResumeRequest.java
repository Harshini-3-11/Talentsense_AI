package com.talentsense.resume.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateResumeRequest {

    @NotBlank(message = "Resume title is required")
    private String title;

    private String rawText;
    private String fileUrl;
    private Boolean isDefault;
}
