package com.talentsense.auth.controller;

import com.talentsense.auth.dto.*;
import com.talentsense.auth.service.AuthService;
import com.talentsense.common.dto.ApiResponse;
import com.talentsense.security.UserPrincipal;
import com.talentsense.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/candidate")
    public ResponseEntity<ApiResponse<AuthResponse>> registerCandidate(@Valid @RequestBody CandidateRegisterRequest request) {
        AuthResponse response = authService.registerCandidate(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Candidate account registered successfully"));
    }

    @PostMapping("/register/recruiter")
    public ResponseEntity<ApiResponse<AuthResponse>> registerRecruiter(@Valid @RequestBody RecruiterRegisterRequest request) {
        AuthResponse response = authService.registerRecruiter(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Recruiter account registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Authentication successful"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Unauthorized request"));
        }
        UserDto userDto = authService.getCurrentUser(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(userDto, "Current user profile fetched"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        // Stateless JWT logout
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", "Logout success"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh() {
        return ResponseEntity.ok(ApiResponse.success("Token refreshed", "Token refreshed successfully"));
    }
}
