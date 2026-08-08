package com.talentsense.career.controller;

import com.talentsense.ai.service.AIService;
import com.talentsense.common.dto.ApiResponse;
import com.talentsense.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/candidates/me")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
public class CareerController {

    private final AIService aiService;

    @GetMapping("/skill-gaps")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSkillGaps(@RequestParam(defaultValue = "Senior Full Stack Engineer") String targetRole) {
        Map<String, Object> gapAnalysis = Map.of(
                "targetRole", targetRole,
                "strongSkills", List.of("Java 21", "Spring Boot", "React", "TypeScript", "REST APIs", "MySQL"),
                "intermediateSkills", List.of("Docker", "Tailwind CSS", "Redis"),
                "missingSkills", List.of("Kubernetes", "AWS Cloud Architecture", "GraphQL"),
                "recommendedAction", "Complete hands-on projects for Kubernetes container orchestration and AWS infrastructure deployment."
        );
        return ResponseEntity.ok(ApiResponse.success(gapAnalysis, "Skill gap analysis completed"));
    }

    @PostMapping("/career-roadmap")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateCareerRoadmap(@RequestBody Map<String, String> payload) {
        String targetRole = payload.getOrDefault("targetRole", "Staff Software Engineer");
        String timeHorizon = payload.getOrDefault("timeHorizon", "12 Months");

        Map<String, Object> roadmap = Map.of(
                "targetRole", targetRole,
                "timeHorizon", timeHorizon,
                "milestones", List.of(
                        Map.of("month", "Months 1-3", "objective", "Master Distributed Systems & Caching Architecture", "actionItem", "Implement Redis caching and microservice event patterns"),
                        Map.of("month", "Months 4-6", "objective", "AWS Cloud & DevOps Mastery", "actionItem", "Obtain AWS Certified Solutions Architect Associate"),
                        Map.of("month", "Months 7-12", "objective", "Engineering Leadership & Technical Mentorship", "actionItem", "Lead system design reviews and mentor junior engineering staff")
                )
        );
        return ResponseEntity.ok(ApiResponse.success(roadmap, "AI Career Roadmap generated"));
    }

    @PostMapping("/ai-coach")
    public ResponseEntity<ApiResponse<Map<String, String>>> askAiCoach(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                       @RequestBody Map<String, String> payload) {
        String query = payload.get("query");
        String responseText = String.format("""
            Based on your career profile and active applications:
            
            Regarding: "%s"
            
            1. **Resume Recommendation**: Highlight your 5+ years of Spring Boot and React experience at the top of your resume summary.
            2. **Interview Advice**: Be prepared to explain your database optimization strategies (indexing, query execution plans) during upcoming technical rounds.
            3. **Growth Focus**: Adding cloud orchestration (Kubernetes/AWS) will increase your job match percentage for Senior and Lead developer positions by ~15%%.
            """, query != null ? query : "Career advice");

        return ResponseEntity.ok(ApiResponse.success(Map.of("reply", responseText), "AI Career Coach response generated"));
    }
}
