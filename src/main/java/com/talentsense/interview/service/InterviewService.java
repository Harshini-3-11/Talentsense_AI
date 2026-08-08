package com.talentsense.interview.service;

import com.talentsense.interview.dto.InterviewDto;
import com.talentsense.interview.dto.ScheduleInterviewRequest;

import java.util.List;
import java.util.Map;

public interface InterviewService {

    InterviewDto scheduleInterview(ScheduleInterviewRequest request);

    List<InterviewDto> getCandidateInterviews(String userId);

    InterviewDto submitFeedback(String interviewId, String feedbackSummary, Integer score);

    List<Map<String, String>> startMockInterview(String jobId);

    Map<String, Object> evaluateMockAnswer(String question, String answer);
}
