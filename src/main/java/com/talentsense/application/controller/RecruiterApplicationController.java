package com.talentsense.application.controller;

import com.talentsense.application.dto.ApplicationDto;
import com.talentsense.application.service.ApplicationService;
import com.talentsense.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recruiter")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
public class RecruiterApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/jobs/{jobId}/applications")
    public ResponseEntity<ApiResponse<List<ApplicationDto>>> getJobApplications(@PathVariable String jobId) {
        List<ApplicationDto> applications = applicationService.getJobApplications(jobId);
        return ResponseEntity.ok(ApiResponse.success(applications, "Job applications fetched successfully"));
    }

    @PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<ApiResponse<ApplicationDto>> updateApplicationStatus(@PathVariable String applicationId,
                                                                                 @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        ApplicationDto updated = applicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.ok(ApiResponse.success(updated, "Application status updated successfully"));
    }
}
