package com.talentsense.candidate.service;

import com.talentsense.candidate.dto.CandidateProfileDto;

public interface CandidateProfileService {

    CandidateProfileDto getProfileByUserId(String userId);

    CandidateProfileDto updateProfile(String userId, CandidateProfileDto updateRequest);
}
