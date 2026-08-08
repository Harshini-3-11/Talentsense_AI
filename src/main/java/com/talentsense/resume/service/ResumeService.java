package com.talentsense.resume.service;

import com.talentsense.resume.dto.CreateResumeRequest;
import com.talentsense.resume.dto.ResumeDto;

import java.util.List;
import java.util.Map;

public interface ResumeService {

    ResumeDto createResume(String userId, CreateResumeRequest request);

    ResumeDto updateResume(String resumeId, String userId, CreateResumeRequest request);

    List<ResumeDto> getCandidateResumes(String userId);

    ResumeDto getResumeById(String resumeId);

    void deleteResume(String resumeId, String userId);

    Map<String, Object> analyzeResume(String resumeId, String userId);

    Map<String, Object> optimizeForJob(String resumeId, String jobId);

    String generateCoverLetter(String resumeId, String jobId);
}
