package com.talentsense.analytics.controller;

import com.talentsense.common.dto.ApiResponse;
import com.talentsense.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnalyticsController {

    @GetMapping("/recruiter/analytics")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRecruiterAnalytics(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Map<String, Object> metrics = Map.of(
                "openJobs", 6,
                "totalApplications", 42,
                "qualifiedCandidates", 28,
                "shortlistRate", 66.6,
                "interviewRate", 32.1,
                "offerRate", 9.5,
                "avgTimeToHireDays", 18,
                "pipelineFunnel", List.of(
                        Map.of("stage", "APPLIED", "count", 42),
                        Map.of("stage", "SCREENING", "count", 28),
                        Map.of("stage", "REVIEW", "count", 18),
                        Map.of("stage", "SHORTLISTED", "count", 12),
                        Map.of("stage", "INTERVIEW", "count", 8),
                        Map.of("stage", "OFFER", "count", 4),
                        Map.of("stage", "HIRED", "count", 3)
                )
        );
        return ResponseEntity.ok(ApiResponse.success(metrics, "Recruiter hiring analytics fetched"));
    }

    @GetMapping("/candidates/me/analytics")
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCandidateAnalytics(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Map<String, Object> metrics = Map.of(
                "totalApplications", 8,
                "interviewsAttended", 3,
                "resumeScoreTrend", List.of(
                        Map.of("month", "Jan", "score", 72),
                        Map.of("month", "Feb", "score", 78),
                        Map.of("month", "Mar", "score", 84),
                        Map.of("month", "Apr", "score", 88)
                ),
                "jobMatchTrend", List.of(
                        Map.of("role", "Full Stack Dev", "match", 88),
                        Map.of("role", "Backend Dev", "match", 92),
                        Map.of("role", "DevOps Engineer", "match", 74)
                )
        );
        return ResponseEntity.ok(ApiResponse.success(metrics, "Candidate career analytics fetched"));
    }
}
