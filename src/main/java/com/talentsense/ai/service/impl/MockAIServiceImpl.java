package com.talentsense.ai.service.impl;

import com.talentsense.ai.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class MockAIServiceImpl implements AIService {

    @Override
    public String generateJobDescription(String title, String industry, String skills, String experience) {
        log.info("Generating AI Job Description for title: {}, industry: {}", title, industry);
        return String.format("""
            ## Position: %s
            **Industry**: %s | **Required Experience**: %s
            
            ### Role Summary
            We are looking for an exceptional %s to join our high-growth engineering team. You will lead key technical initiatives, design resilient backend architecture, and collaborate across cross-functional teams.
            
            ### Core Responsibilities
            - Architect, develop, and deploy scalable cloud-native web APIs and services.
            - Ensure clean code standards, comprehensive automated testing, and security best practices.
            - Collaborate with product managers, UX designers, and hiring stakeholders.
            - Mentor junior engineers and participate in architectural reviews.
            
            ### Key Required Skills
            - %s
            
            ### Preferred Competencies
            - Strong problem-solving mindset and clear communication.
            - Production experience with CI/CD pipelines, containerization, and relational databases.
            """, title, industry != null ? industry : "Technology", experience != null ? experience : "3+ Years", title, skills != null ? skills : "Java, Spring Boot, React, MySQL, REST APIs");
    }

    @Override
    public Map<String, Object> analyzeResume(String resumeText) {
        log.info("Analyzing resume text length: {}", resumeText != null ? resumeText.length() : 0);

        Map<String, Object> result = new HashMap<>();
        result.put("overallScore", 86);
        result.put("clarityScore", 90);
        result.put("atsCompatibility", 88);
        result.put("strengths", List.of(
                "Strong technical stack with relevant backend and frontend experience.",
                "Clear project descriptions with measurable outcomes.",
                "Well-structured experience section."
        ));
        result.put("improvements", List.of(
                "Add quantifiable metrics to key project achievements (e.g. % performance increase).",
                "Include cloud certification details if applicable.",
                "Highlight leadership or cross-functional initiative participation."
        ));
        result.put("extractedSkills", List.of("Java", "Spring Boot", "TypeScript", "React", "MySQL", "REST APIs", "Git", "Docker"));
        return result;
    }

    @Override
    public Map<String, Object> matchCandidateToJob(String resumeText, String jobDescription) {
        log.info("Calculating candidate-to-job match assessment");

        Map<String, Object> matchResult = new HashMap<>();
        matchResult.put("matchScore", 88);
        matchResult.put("confidence", "HIGH");

        List<Map<String, String>> matchedSkills = List.of(
                Map.of("skill", "Java 21 & Spring Boot", "status", "STRONG", "evidence", "5+ years of backend development"),
                Map.of("skill", "React & TypeScript", "status", "STRONG", "evidence", "Full-stack client portal building"),
                Map.of("skill", "MySQL Database", "status", "MATCHED", "evidence", "Relational schema design and indexing")
        );

        List<Map<String, String>> gaps = List.of(
                Map.of("requirement", "Kubernetes", "status", "UNKNOWN", "recommendation", "Verify container orchestration experience in technical interview")
        );

        matchResult.put("matchedSkills", matchedSkills);
        matchResult.put("gaps", gaps);
        matchResult.put("verificationQuestions", List.of(
                "Describe your production deployment pipeline experience.",
                "How do you handle zero-downtime database migrations?"
        ));

        return matchResult;
    }

    @Override
    public List<Map<String, String>> generateInterviewQuestions(String jobTitle, String jobDescription) {
        log.info("Generating AI interview questions for: {}", jobTitle);

        return List.of(
                Map.of("category", "TECHNICAL", "question", "Explain your approach to designing a high-throughput REST API in Spring Boot.", "sampleAnswerCriteria", "Mentions DTO isolation, caching, JPA query optimization, and proper HTTP status codes."),
                Map.of("category", "ARCHITECTURE", "question", "How do you handle database migration rollbacks safely in production?", "sampleAnswerCriteria", "Mentions Flyway/Liquibase versioning, backward-compatible DDL changes, and transaction safety."),
                Map.of("category", "BEHAVIORAL", "question", "Describe a time when you had to resolve an architectural disagreement with a team member.", "sampleAnswerCriteria", "Demonstrates objective benchmark evidence, active listening, and collaborative decision making.")
        );
    }

    @Override
    public Map<String, Object> evaluateMockAnswer(String question, String answer) {
        log.info("Evaluating mock interview answer");

        Map<String, Object> eval = new HashMap<>();
        eval.put("score", 4); // Out of 5
        eval.put("communicationRating", "EXCELLENT");
        eval.put("technicalAccuracy", "HIGH");
        eval.put("starStructureScore", 85);
        eval.put("strengths", List.of("Clear context setting", "Direct technical explanation"));
        eval.put("constructiveFeedback", "Consider adding concrete business impact metrics to complete the Result phase of STAR.");
        return eval;
    }
}
