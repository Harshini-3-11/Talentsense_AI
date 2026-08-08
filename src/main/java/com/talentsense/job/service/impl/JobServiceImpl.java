package com.talentsense.job.service.impl;

import com.talentsense.ai.service.AIService;
import com.talentsense.job.dto.CreateJobRequest;
import com.talentsense.job.dto.JobDto;
import com.talentsense.job.entity.Job;
import com.talentsense.job.entity.Job.JobStatus;
import com.talentsense.job.repository.JobRepository;
import com.talentsense.job.service.JobService;
import com.talentsense.organization.entity.Organization;
import com.talentsense.recruiter.entity.RecruiterProfile;
import com.talentsense.recruiter.repository.RecruiterProfileRepository;
import com.talentsense.user.entity.User;
import com.talentsense.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final AIService aiService;

    @Override
    @Transactional
    public JobDto createJob(String userId, CreateJobRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Organization org = recruiterProfileRepository.findByUser(user)
                .map(RecruiterProfile::getOrganization)
                .orElse(null);

        Job job = Job.builder()
                .title(request.getTitle())
                .department(request.getDepartment())
                .description(request.getDescription())
                .location(request.getLocation())
                .organization(org)
                .createdByUser(user)
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .currency(request.getCurrency() != null ? request.getCurrency() : "USD")
                .experienceMin(request.getExperienceMin() != null ? request.getExperienceMin() : 0)
                .experienceMax(request.getExperienceMax())
                .industry(request.getIndustry())
                .status(JobStatus.DRAFT)
                .build();

        if (request.getRemoteType() != null) {
            try {
                job.setRemoteType(Job.RemoteType.valueOf(request.getRemoteType().toUpperCase()));
            } catch (Exception ignored) {}
        }

        if (request.getEmploymentType() != null) {
            try {
                job.setEmploymentType(Job.EmploymentType.valueOf(request.getEmploymentType().toUpperCase()));
            } catch (Exception ignored) {}
        }

        if (request.getSeniority() != null) {
            try {
                job.setSeniority(Job.Seniority.valueOf(request.getSeniority().toUpperCase()));
            } catch (Exception ignored) {}
        }

        Job savedJob = jobRepository.save(job);
        return mapToDto(savedJob);
    }

    @Override
    @Transactional
    public JobDto updateJob(String jobId, String userId, CreateJobRequest request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));

        if (request.getTitle() != null) job.setTitle(request.getTitle());
        if (request.getDepartment() != null) job.setDepartment(request.getDepartment());
        if (request.getDescription() != null) job.setDescription(request.getDescription());
        if (request.getLocation() != null) job.setLocation(request.getLocation());
        if (request.getSalaryMin() != null) job.setSalaryMin(request.getSalaryMin());
        if (request.getSalaryMax() != null) job.setSalaryMax(request.getSalaryMax());
        if (request.getIndustry() != null) job.setIndustry(request.getIndustry());

        Job updatedJob = jobRepository.save(job);
        return mapToDto(updatedJob);
    }

    @Override
    @Transactional
    public JobDto publishJob(String jobId, String userId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));
        job.setStatus(JobStatus.PUBLISHED);
        return mapToDto(jobRepository.save(job));
    }

    @Override
    @Transactional
    public JobDto closeJob(String jobId, String userId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));
        job.setStatus(JobStatus.CLOSED);
        return mapToDto(jobRepository.save(job));
    }

    @Override
    @Transactional(readOnly = true)
    public JobDto getJobById(String jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));
        return mapToDto(job);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobDto> getPublishedJobs(String keyword, String location, String industry, Pageable pageable) {
        Page<Job> jobs = jobRepository.searchJobs(keyword, location, industry, pageable);
        return jobs.map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobDto> getRecruiterJobs(String userId) {
        List<Job> jobs = jobRepository.findByCreatedByUserId(userId);
        return jobs.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public String generateJobDescription(String title, String industry, String skills, String experience) {
        return aiService.generateJobDescription(title, industry, skills, experience);
    }

    private JobDto mapToDto(Job job) {
        return JobDto.builder()
                .id(job.getId())
                .organizationId(job.getOrganization() != null ? job.getOrganization().getId() : null)
                .companyName(job.getOrganization() != null ? job.getOrganization().getName() : "TalentSense AI")
                .createdByUserId(job.getCreatedByUser().getId())
                .title(job.getTitle())
                .department(job.getDepartment())
                .description(job.getDescription())
                .location(job.getLocation())
                .remoteType(job.getRemoteType().name())
                .employmentType(job.getEmploymentType().name())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .currency(job.getCurrency())
                .experienceMin(job.getExperienceMin())
                .experienceMax(job.getExperienceMax())
                .seniority(job.getSeniority().name())
                .industry(job.getIndustry())
                .status(job.getStatus().name())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}
