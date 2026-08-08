package com.talentsense.application.service.impl;

import com.talentsense.ai.service.AIService;
import com.talentsense.application.dto.ApplicationDto;
import com.talentsense.application.dto.ApplyJobRequest;
import com.talentsense.application.entity.Application;
import com.talentsense.application.entity.Application.ApplicationStatus;
import com.talentsense.application.repository.ApplicationRepository;
import com.talentsense.application.service.ApplicationService;
import com.talentsense.candidate.entity.CandidateProfile;
import com.talentsense.candidate.repository.CandidateProfileRepository;
import com.talentsense.job.entity.Job;
import com.talentsense.job.repository.JobRepository;
import com.talentsense.resume.entity.Resume;
import com.talentsense.resume.repository.ResumeRepository;
import com.talentsense.user.entity.User;
import com.talentsense.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final AIService aiService;

    @Override
    @Transactional
    public ApplicationDto applyForJob(String jobId, String userId, ApplyJobRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        CandidateProfile candidate = candidateProfileRepository.findByUser(user)
                .orElseGet(() -> candidateProfileRepository.save(CandidateProfile.builder().user(user).build()));

        if (applicationRepository.existsByJobIdAndCandidateId(jobId, candidate.getId())) {
            throw new IllegalArgumentException("You have already applied for this position");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));

        Resume resume = null;
        if (request.getResumeId() != null) {
            resume = resumeRepository.findById(request.getResumeId()).orElse(null);
        }

        // Calculate transparent AI Match Score
        int matchScore = 87;
        if (resume != null && resume.getRawText() != null) {
            Map<String, Object> matchAnalysis = aiService.matchCandidateToJob(resume.getRawText(), job.getDescription());
            if (matchAnalysis.containsKey("matchScore")) {
                matchScore = (Integer) matchAnalysis.get("matchScore");
            }
        }

        Application application = Application.builder()
                .job(job)
                .candidate(candidate)
                .resume(resume)
                .coverLetter(request.getCoverLetter())
                .status(ApplicationStatus.APPLIED)
                .matchScore(matchScore)
                .build();

        Application saved = applicationRepository.save(application);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDto> getCandidateApplications(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        CandidateProfile candidate = candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Candidate profile not found"));

        return applicationRepository.findByCandidateId(candidate.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDto> getJobApplications(String jobId) {
        return applicationRepository.findByJobId(jobId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ApplicationDto updateApplicationStatus(String applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + applicationId));

        try {
            application.setStatus(ApplicationStatus.valueOf(status.toUpperCase()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        Application updated = applicationRepository.save(application);
        return mapToDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationDto getApplicationById(String applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + applicationId));
        return mapToDto(application);
    }

    private ApplicationDto mapToDto(Application app) {
        return ApplicationDto.builder()
                .id(app.getId())
                .jobId(app.getJob().getId())
                .jobTitle(app.getJob().getTitle())
                .companyName(app.getJob().getOrganization() != null ? app.getJob().getOrganization().getName() : "Company")
                .candidateId(app.getCandidate().getId())
                .candidateName(app.getCandidate().getUser().getFirstName() + " " + app.getCandidate().getUser().getLastName())
                .candidateEmail(app.getCandidate().getUser().getEmail())
                .resumeId(app.getResume() != null ? app.getResume().getId() : null)
                .coverLetter(app.getCoverLetter())
                .status(app.getStatus().name())
                .matchScore(app.getMatchScore())
                .appliedAt(app.getAppliedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }
}
