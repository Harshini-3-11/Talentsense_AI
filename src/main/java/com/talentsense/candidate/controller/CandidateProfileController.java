package com.talentsense.candidate.controller;

import com.talentsense.candidate.dto.CandidateProfileDto;
import com.talentsense.candidate.service.CandidateProfileService;
import com.talentsense.common.dto.ApiResponse;
import com.talentsense.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CandidateProfileDto>> getMyProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        CandidateProfileDto profile = candidateProfileService.getProfileByUserId(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(profile, "Candidate profile fetched successfully"));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CandidateProfileDto>> updateMyProfile(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                             @RequestBody CandidateProfileDto updateRequest) {
        CandidateProfileDto updatedProfile = candidateProfileService.updateProfile(userPrincipal.getId(), updateRequest);
        return ResponseEntity.ok(ApiResponse.success(updatedProfile, "Candidate profile updated successfully"));
    }
}
