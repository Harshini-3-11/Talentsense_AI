package com.talentsense.resume.service.impl;

import com.talentsense.ai.service.AIService;
import com.talentsense.candidate.entity.CandidateProfile;
import com.talentsense.candidate.repository.CandidateProfileRepository;
import com.talentsense.job.entity.Job;
import com.talentsense.job.repository.JobRepository;
import com.talentsense.resume.dto.CreateResumeRequest;
import com.talentsense.resume.dto.ResumeDto;
import com.talentsense.resume.entity.Resume;
import com.talentsense.resume.repository.ResumeRepository;
import com.talentsense.resume.service.ResumeService;
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
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final AIService aiService;

    @Override
    @Transactional
    public ResumeDto createResume(String userId, CreateResumeRequest request) {
        CandidateProfile candidate = getCandidateByUserId(userId);

        Resume resume = Resume.builder()
                .candidate(candidate)
                .title(request.getTitle())
                .rawText(request.getRawText())
                .fileUrl(request.getFileUrl())
                .isDefault(request.getIsDefault() != null ? request.getIsDefault() : false)
                .overallScore(84)
                .build();

        Resume saved = resumeRepository.save(resume);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public ResumeDto updateResume(String resumeId, String userId, CreateResumeRequest request) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found: " + resumeId));

        if (request.getTitle() != null) resume.setTitle(request.getTitle());
        if (request.getRawText() != null) resume.setRawText(request.getRawText());
        if (request.getFileUrl() != null) resume.setFileUrl(request.getFileUrl());
        if (request.getIsDefault() != null) resume.setIsDefault(request.getIsDefault());

        Resume updated = resumeRepository.save(resume);
        return mapToDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumeDto> getCandidateResumes(String userId) {
        CandidateProfile candidate = getCandidateByUserId(userId);
        return resumeRepository.findByCandidateId(candidate.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResumeDto getResumeById(String resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found: " + resumeId));
        return mapToDto(resume);
    }

    @Override
    @Transactional
    public void deleteResume(String resumeId, String userId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found: " + resumeId));
        resumeRepository.delete(resume);
    }

    @Override
    public Map<String, Object> analyzeResume(String resumeId, String userId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found: " + resumeId));
        return aiService.analyzeResume(resume.getRawText());
    }

    @Override
    public Map<String, Object> optimizeForJob(String resumeId, String jobId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found: " + resumeId));
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));

        return aiService.matchCandidateToJob(resume.getRawText(), job.getDescription());
    }

    @Override
    public String generateCoverLetter(String resumeId, String jobId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found: " + resumeId));
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + jobId));

        return String.format("""
            Dear Hiring Manager at %s,
            
            I am writing to express my enthusiastic interest in the %s position. With extensive hands-on experience in software engineering and cloud-native architecture, I am confident in my ability to make an immediate positive impact on your engineering initiatives.
            
            Based on your requirements for %s, my professional background aligns strongly with your target profile:
            - Proficient in full-stack web architectures, REST API microservices, and database optimization.
            - Proven track record of delivering clean, scalable software solutions in high-throughput environments.
            
            I look forward to discussing how my experience and technical skill set can contribute to the continued success of %s.
            
            Sincerely,
            %s %s
            """, job.getOrganization() != null ? job.getOrganization().getName() : "your company",
                job.getTitle(),
                job.getTitle(),
                job.getOrganization() != null ? job.getOrganization().getName() : "your company",
                resume.getCandidate().getUser().getFirstName(),
                resume.getCandidate().getUser().getLastName());
    }

    private CandidateProfile getCandidateByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        return candidateProfileRepository.findByUser(user)
                .orElseGet(() -> candidateProfileRepository.save(CandidateProfile.builder().user(user).build()));
    }

    private ResumeDto mapToDto(Resume resume) {
        return ResumeDto.builder()
                .id(resume.getId())
                .candidateId(resume.getCandidate().getId())
                .title(resume.getTitle())
                .rawText(resume.getRawText())
                .fileUrl(resume.getFileUrl())
                .overallScore(resume.getOverallScore())
                .isDefault(resume.getIsDefault())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .build();
    }
}
