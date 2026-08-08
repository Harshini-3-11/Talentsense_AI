package com.talentsense.interview.controller;

import com.talentsense.common.dto.ApiResponse;
import com.talentsense.interview.dto.InterviewDto;
import com.talentsense.interview.dto.ScheduleInterviewRequest;
import com.talentsense.interview.service.InterviewService;
import com.talentsense.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/schedule")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InterviewDto>> scheduleInterview(@Valid @RequestBody ScheduleInterviewRequest request) {
        InterviewDto interview = interviewService.scheduleInterview(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(interview, "Interview scheduled successfully"));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<InterviewDto>>> getMyInterviews(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<InterviewDto> interviews = interviewService.getCandidateInterviews(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(interviews, "Candidate interviews fetched"));
    }

    @PostMapping("/{id}/feedback")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('HIRING_MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InterviewDto>> submitFeedback(@PathVariable String id,
                                                                     @RequestBody Map<String, Object> payload) {
        String feedback = (String) payload.get("feedbackSummary");
        Integer score = (Integer) payload.get("overallScore");
        InterviewDto updated = interviewService.submitFeedback(id, feedback, score);
        return ResponseEntity.ok(ApiResponse.success(updated, "Interview feedback submitted successfully"));
    }

    @PostMapping("/mock/questions")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getMockQuestions(@RequestParam(required = false) String jobId) {
        List<Map<String, String>> questions = interviewService.startMockInterview(jobId);
        return ResponseEntity.ok(ApiResponse.success(questions, "AI Mock Interview questions generated"));
    }

    @PostMapping("/mock/evaluate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> evaluateMockAnswer(@RequestBody Map<String, String> payload) {
        String question = payload.get("question");
        String answer = payload.get("answer");
        Map<String, Object> evaluation = interviewService.evaluateMockAnswer(question, answer);
        return ResponseEntity.ok(ApiResponse.success(evaluation, "AI STAR answer evaluation completed"));
    }
}
