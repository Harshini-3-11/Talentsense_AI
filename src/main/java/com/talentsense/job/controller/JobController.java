package com.talentsense.job.controller;

import com.talentsense.common.dto.ApiResponse;
import com.talentsense.job.dto.JobDto;
import com.talentsense.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobDto>>> getJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String industry,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<JobDto> jobs = jobService.getPublishedJobs(keyword, location, industry,
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
        return ResponseEntity.ok(ApiResponse.success(jobs, "Jobs retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobDto>> getJobById(@PathVariable String id) {
        JobDto job = jobService.getJobById(id);
        return ResponseEntity.ok(ApiResponse.success(job, "Job details retrieved"));
    }
}
