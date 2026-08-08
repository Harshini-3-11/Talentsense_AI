package com.talentsense.common.controller;

import com.talentsense.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHealthStatus() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("service", "TalentSense AI Backend API");
        healthInfo.put("version", "1.0.0");
        healthInfo.put("database", "CONNECTED");
        healthInfo.put("timestamp", java.time.Instant.now().toString());

        return ResponseEntity.ok(ApiResponse.success(healthInfo, "TalentSense AI API is online"));
    }
}
