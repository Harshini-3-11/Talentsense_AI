package com.talentsense.recruiter.repository;

import com.talentsense.recruiter.entity.RecruiterProfile;
import com.talentsense.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, String> {

    Optional<RecruiterProfile> findByUser(User user);

    Optional<RecruiterProfile> findByUserId(String userId);
}
