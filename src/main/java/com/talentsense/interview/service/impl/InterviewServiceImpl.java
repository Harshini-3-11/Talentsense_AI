package com.talentsense.interview.service.impl;

import com.talentsense.ai.service.AIService;
import com.talentsense.application.entity.Application;
import com.talentsense.application.repository.ApplicationRepository;
import com.talentsense.candidate.entity.CandidateProfile;
import com.talentsense.candidate.repository.CandidateProfileRepository;
import com.talentsense.interview.dto.InterviewDto;
import com.talentsense.interview.dto.ScheduleInterviewRequest;
import com.talentsense.interview.entity.Interview;
import com.talentsense.interview.entity.Interview.InterviewStatus;
import com.talentsense.interview.entity.Interview.InterviewType;
import com.talentsense.interview.repository.InterviewRepository;
import com.talentsense.interview.service.InterviewService;
import com.talentsense.job.entity.Job;
import com.talentsense.job.repository.JobRepository;
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
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final AIService aiService;

    @Override
    @Transactional
    public InterviewDto scheduleInterview(ScheduleInterviewRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException("Application not found: " + request.getApplicationId()));

        Interview interview = Interview.builder()
                .application(application)
                .candidate(application.getCandidate())
                .job(application.getJob())
                .interviewType(InterviewType.valueOf(request.getInterviewType().toUpperCase()))
                .status(InterviewStatus.SCHEDULED)
                .scheduledAt(request.getScheduledAt() != null ? request.getScheduledAt() : java.time.LocalDateTime.now().plusDays(1))
                .durationMinutes(request.getDurationMinutes() != null ? request.getDurationMinutes() : 45)
                .build();

        Interview saved = interviewRepository.save(interview);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewDto> getCandidateInterviews(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        CandidateProfile candidate = candidateProfileRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Candidate profile not found"));

        return interviewRepository.findByCandidateId(candidate.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InterviewDto submitFeedback(String interviewId, String feedbackSummary, Integer score) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found: " + interviewId));

        interview.setFeedbackSummary(feedbackSummary);
        interview.setOverallScore(score);
        interview.setStatus(InterviewStatus.COMPLETED);

        return mapToDto(interviewRepository.save(interview));
    }

    @Override
    public List<Map<String, String>> startMockInterview(String jobId) {
        Job job = jobRepository.findById(jobId).orElse(null);
        String title = job != null ? job.getTitle() : "Software Engineer";
        String desc = job != null ? job.getDescription() : "Full Stack Developer";

        return aiService.generateInterviewQuestions(title, desc);
    }

    @Override
    public Map<String, Object> evaluateMockAnswer(String question, String answer) {
        return aiService.evaluateMockAnswer(question, answer);
    }

    private InterviewDto mapToDto(Interview interview) {
        return InterviewDto.builder()
                .id(interview.getId())
                .applicationId(interview.getApplication().getId())
                .candidateId(interview.getCandidate().getId())
                .candidateName(interview.getCandidate().getUser().getFirstName() + " " + interview.getCandidate().getUser().getLastName())
                .jobId(interview.getJob().getId())
                .jobTitle(interview.getJob().getTitle())
                .interviewType(interview.getInterviewType().name())
                .status(interview.getStatus().name())
                .scheduledAt(interview.getScheduledAt())
                .durationMinutes(interview.getDurationMinutes())
                .feedbackSummary(interview.getFeedbackSummary())
                .overallScore(interview.getOverallScore())
                .build();
    }
}
