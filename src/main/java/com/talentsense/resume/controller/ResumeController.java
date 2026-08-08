package com.talentsense.resume.controller;

import com.talentsense.common.dto.ApiResponse;
import com.talentsense.resume.dto.CreateResumeRequest;
import com.talentsense.resume.dto.ResumeDto;
import com.talentsense.resume.service.ResumeService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping("/candidates/me/resumes")
    public ResponseEntity<ApiResponse<List<ResumeDto>>> getMyResumes(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<ResumeDto> resumes = resumeService.getCandidateResumes(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(resumes, "Resumes fetched successfully"));
    }

    @PostMapping("/candidates/me/resumes")
    public ResponseEntity<ApiResponse<ResumeDto>> createResume(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                @Valid @RequestBody CreateResumeRequest request) {
        ResumeDto resume = resumeService.createResume(userPrincipal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(resume, "Resume created successfully"));
    }

    @PutMapping("/resumes/{id}")
    public ResponseEntity<ApiResponse<ResumeDto>> updateResume(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                @PathVariable String id,
                                                                @RequestBody CreateResumeRequest request) {
        ResumeDto resume = resumeService.updateResume(id, userPrincipal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(resume, "Resume updated successfully"));
    }

    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<ApiResponse<String>> deleteResume(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                            @PathVariable String id) {
        resumeService.deleteResume(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Resume deleted", "Resume deleted successfully"));
    }

    @PostMapping("/resumes/{id}/analyze")
    public ResponseEntity<ApiResponse<Map<String, Object>>> analyzeResume(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                          @PathVariable String id) {
        Map<String, Object> analysis = resumeService.analyzeResume(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(analysis, "AI Resume Analysis completed"));
    }

    @PostMapping("/resumes/{id}/optimize")
    public ResponseEntity<ApiResponse<Map<String, Object>>> optimizeForJob(@PathVariable String id,
                                                                          @RequestParam String jobId) {
        Map<String, Object> optimization = resumeService.optimizeForJob(id, jobId);
        return ResponseEntity.ok(ApiResponse.success(optimization, "Job-specific optimization completed"));
    }

    @PostMapping("/resumes/{id}/cover-letter")
    public ResponseEntity<ApiResponse<Map<String, String>>> generateCoverLetter(@PathVariable String id,
                                                                                @RequestParam String jobId) {
        String coverLetter = resumeService.generateCoverLetter(id, jobId);
        return ResponseEntity.ok(ApiResponse.success(Map.of("coverLetter", coverLetter), "AI Cover Letter generated"));
    }
}
