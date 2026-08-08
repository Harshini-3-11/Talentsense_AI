package com.talentsense.candidate.repository;

import com.talentsense.candidate.entity.CandidateProfile;
import com.talentsense.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, String> {

    Optional<CandidateProfile> findByUser(User user);

    Optional<CandidateProfile> findByUserId(String userId);
}
