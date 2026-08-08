package com.talentsense.application.service;

import com.talentsense.application.dto.ApplicationDto;
import com.talentsense.application.dto.ApplyJobRequest;

import java.util.List;

public interface ApplicationService {

    ApplicationDto applyForJob(String jobId, String userId, ApplyJobRequest request);

    List<ApplicationDto> getCandidateApplications(String userId);

    List<ApplicationDto> getJobApplications(String jobId);

    ApplicationDto updateApplicationStatus(String applicationId, String status);

    ApplicationDto getApplicationById(String applicationId);
}
