package com.talentsense.auth.service;

import com.talentsense.auth.dto.*;
import com.talentsense.user.dto.UserDto;

public interface AuthService {

    AuthResponse registerCandidate(CandidateRegisterRequest request);

    AuthResponse registerRecruiter(RecruiterRegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserDto getCurrentUser(String userId);
}
