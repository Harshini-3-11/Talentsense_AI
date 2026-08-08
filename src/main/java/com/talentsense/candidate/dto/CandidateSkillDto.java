package com.talentsense.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateSkillDto {
    private String id;
    private String name;
    private String category;
    private String proficiency;
    private Integer yearsExperience;
}
