package com.talentsense.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileDto {

    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String headline;
    private String summary;
    private String location;
    private String phone;
    private String githubUrl;
    private String linkedinUrl;
    private String portfolioUrl;
    private Integer careerReadinessScore;
    private List<CandidateSkillDto> skills;
    private List<CandidateExperienceDto> experiences;
}
