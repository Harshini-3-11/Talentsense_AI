package com.talentsense.application.controller;

import com.talentsense.application.dto.ApplicationDto;
import com.talentsense.application.dto.ApplyJobRequest;
import com.talentsense.application.service.ApplicationService;
import com.talentsense.common.dto.ApiResponse;
import com.talentsense.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/jobs/{jobId}/apply")
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ApplicationDto>> applyForJob(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                   @PathVariable String jobId,
                                                                   @RequestBody ApplyJobRequest request) {
        ApplicationDto application = applicationService.applyForJob(jobId, userPrincipal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(application, "Job application submitted successfully"));
    }

    @GetMapping("/candidates/me/applications")
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ApplicationDto>>> getMyApplications(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<ApplicationDto> applications = applicationService.getCandidateApplications(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(applications, "Applications fetched successfully"));
    }
}
