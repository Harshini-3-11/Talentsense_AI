package com.talentsense.candidate.service.impl;

import com.talentsense.candidate.dto.CandidateProfileDto;
import com.talentsense.candidate.entity.CandidateProfile;
import com.talentsense.candidate.repository.CandidateProfileRepository;
import com.talentsense.candidate.service.CandidateProfileService;
import com.talentsense.user.entity.User;
import com.talentsense.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CandidateProfileServiceImpl implements CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public CandidateProfileDto getProfileByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        CandidateProfile profile = candidateProfileRepository.findByUser(user)
                .orElseGet(() -> candidateProfileRepository.save(CandidateProfile.builder()
                        .user(user)
                        .headline("Software Professional")
                        .careerReadinessScore(75)
                        .build()));

        return mapToDto(profile, user);
    }

    @Override
    @Transactional
    public CandidateProfileDto updateProfile(String userId, CandidateProfileDto updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        CandidateProfile profile = candidateProfileRepository.findByUser(user)
                .orElseGet(() -> CandidateProfile.builder().user(user).build());

        if (updateRequest.getHeadline() != null) profile.setHeadline(updateRequest.getHeadline());
        if (updateRequest.getSummary() != null) profile.setSummary(updateRequest.getSummary());
        if (updateRequest.getLocation() != null) profile.setLocation(updateRequest.getLocation());
        if (updateRequest.getPhone() != null) profile.setPhone(updateRequest.getPhone());
        if (updateRequest.getGithubUrl() != null) profile.setGithubUrl(updateRequest.getGithubUrl());
        if (updateRequest.getLinkedinUrl() != null) profile.setLinkedinUrl(updateRequest.getLinkedinUrl());
        if (updateRequest.getPortfolioUrl() != null) profile.setPortfolioUrl(updateRequest.getPortfolioUrl());

        CandidateProfile updatedProfile = candidateProfileRepository.save(profile);
        return mapToDto(updatedProfile, user);
    }

    private CandidateProfileDto mapToDto(CandidateProfile profile, User user) {
        return CandidateProfileDto.builder()
                .id(profile.getId())
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .headline(profile.getHeadline())
                .summary(profile.getSummary())
                .location(profile.getLocation())
                .phone(profile.getPhone())
                .githubUrl(profile.getGithubUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .portfolioUrl(profile.getPortfolioUrl())
                .careerReadinessScore(profile.getCareerReadinessScore() != null ? profile.getCareerReadinessScore() : 75)
                .skills(new ArrayList<>())
                .experiences(new ArrayList<>())
                .build();
    }
}
