package com.talentsense.job.controller;

import com.talentsense.common.dto.ApiResponse;
import com.talentsense.job.dto.CreateJobRequest;
import com.talentsense.job.dto.JobDto;
import com.talentsense.job.service.JobService;
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
@RequestMapping("/api/recruiter/jobs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
public class RecruiterJobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobDto>>> getMyJobs(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<JobDto> jobs = jobService.getRecruiterJobs(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(jobs, "Recruiter jobs fetched successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JobDto>> createJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                         @Valid @RequestBody CreateJobRequest request) {
        JobDto job = jobService.createJob(userPrincipal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(job, "Job draft created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobDto>> updateJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                         @PathVariable String id,
                                                         @RequestBody CreateJobRequest request) {
        JobDto job = jobService.updateJob(id, userPrincipal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(job, "Job updated successfully"));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<JobDto>> publishJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                          @PathVariable String id) {
        JobDto job = jobService.publishJob(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(job, "Job published successfully"));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<ApiResponse<JobDto>> closeJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                        @PathVariable String id) {
        JobDto job = jobService.closeJob(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(job, "Job closed successfully"));
    }

    @PostMapping("/generate-jd")
    public ResponseEntity<ApiResponse<Map<String, String>>> generateAiJobDescription(@RequestBody Map<String, String> payload) {
        String title = payload.get("title");
        String industry = payload.get("industry");
        String skills = payload.get("skills");
        String experience = payload.get("experience");

        String generatedJd = jobService.generateJobDescription(title, industry, skills, experience);
        return ResponseEntity.ok(ApiResponse.success(Map.of("generatedDescription", generatedJd), "AI Job Description generated"));
    }
}
