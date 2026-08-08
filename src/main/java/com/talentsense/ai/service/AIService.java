package com.talentsense.ai.service;

import java.util.List;
import java.util.Map;

public interface AIService {

    String generateJobDescription(String title, String industry, String skills, String experience);

    Map<String, Object> analyzeResume(String resumeText);

    Map<String, Object> matchCandidateToJob(String resumeText, String jobDescription);

    List<Map<String, String>> generateInterviewQuestions(String jobTitle, String jobDescription);

    Map<String, Object> evaluateMockAnswer(String question, String answer);
}
