package com.talentsense.auth.service.impl;

import com.talentsense.auth.dto.*;
import com.talentsense.auth.service.AuthService;
import com.talentsense.candidate.entity.CandidateProfile;
import com.talentsense.candidate.repository.CandidateProfileRepository;
import com.talentsense.organization.entity.Organization;
import com.talentsense.organization.repository.OrganizationRepository;
import com.talentsense.recruiter.entity.RecruiterProfile;
import com.talentsense.recruiter.repository.RecruiterProfileRepository;
import com.talentsense.security.JwtTokenProvider;
import com.talentsense.user.dto.UserDto;
import com.talentsense.user.entity.Role;
import com.talentsense.user.entity.User;
import com.talentsense.user.repository.RoleRepository;
import com.talentsense.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final OrganizationRepository organizationRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public AuthResponse registerCandidate(CandidateRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email address is already in use");
        }

        Role candidateRole = roleRepository.findByName("ROLE_CANDIDATE")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .id("r-101")
                        .name("ROLE_CANDIDATE")
                        .description("Job candidate / applicant")
                        .build()));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .userType(User.UserType.CANDIDATE)
                .roles(Set.of(candidateRole))
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        // Auto-create Candidate Profile
        CandidateProfile candidateProfile = CandidateProfile.builder()
                .user(savedUser)
                .headline("Software Professional")
                .careerReadinessScore(70)
                .build();

        candidateProfileRepository.save(candidateProfile);

        String token = tokenProvider.generateTokenFromUserId(savedUser.getId(), savedUser.getEmail());

        return AuthResponse.builder()
                .token(token)
                .user(mapToUserDto(savedUser))
                .build();
    }

    @Override
    @Transactional
    public AuthResponse registerRecruiter(RecruiterRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email address is already in use");
        }

        Role recruiterRole = roleRepository.findByName("ROLE_RECRUITER")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .id("r-102")
                        .name("ROLE_RECRUITER")
                        .description("Recruiter / talent acquisition officer")
                        .build()));

        // Find or create organization
        Organization organization = organizationRepository.findByNameIgnoreCase(request.getCompanyName())
                .orElseGet(() -> organizationRepository.save(Organization.builder()
                        .name(request.getCompanyName())
                        .companyDomain(extractDomain(request.getEmail()))
                        .build()));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .userType(User.UserType.RECRUITER)
                .roles(Set.of(recruiterRole))
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        // Create Recruiter Profile
        RecruiterProfile recruiterProfile = RecruiterProfile.builder()
                .user(savedUser)
                .organization(organization)
                .jobTitle(request.getJobTitle())
                .build();

        recruiterProfileRepository.save(recruiterProfile);

        String token = tokenProvider.generateTokenFromUserId(savedUser.getId(), savedUser.getEmail());

        UserDto userDto = mapToUserDto(savedUser);
        userDto.setCompanyName(organization.getName());
        userDto.setJobTitle(request.getJobTitle());

        return AuthResponse.builder()
                .token(token)
                .user(userDto)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase().trim(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return AuthResponse.builder()
                .token(token)
                .user(mapToUserDto(user))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        UserDto userDto = mapToUserDto(user);

        if (user.getUserType() == User.UserType.RECRUITER) {
            recruiterProfileRepository.findByUser(user).ifPresent(rp -> {
                userDto.setJobTitle(rp.getJobTitle());
                if (rp.getOrganization() != null) {
                    userDto.setCompanyName(rp.getOrganization().getName());
                }
            });
        }

        return userDto;
    }

    private UserDto mapToUserDto(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userType(user.getUserType().name())
                .roles(roleNames)
                .build();
    }

    private String extractDomain(String email) {
        if (email != null && email.contains("@")) {
            return email.substring(email.indexOf("@") + 1);
        }
        return "company.com";
    }
}
