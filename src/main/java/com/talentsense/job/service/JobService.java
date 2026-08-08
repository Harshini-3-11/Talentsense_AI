package com.talentsense.job.service;

import com.talentsense.job.dto.CreateJobRequest;
import com.talentsense.job.dto.JobDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobService {

    JobDto createJob(String userId, CreateJobRequest request);

    JobDto updateJob(String jobId, String userId, CreateJobRequest request);

    JobDto publishJob(String jobId, String userId);

    JobDto closeJob(String jobId, String userId);

    JobDto getJobById(String jobId);

    Page<JobDto> getPublishedJobs(String keyword, String location, String industry, Pageable pageable);

    List<JobDto> getRecruiterJobs(String userId);

    String generateJobDescription(String title, String industry, String skills, String experience);
}
