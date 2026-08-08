package com.talentsense.application.repository;

import com.talentsense.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {

    List<Application> findByCandidateId(String candidateId);

    List<Application> findByJobId(String jobId);

    boolean existsByJobIdAndCandidateId(String jobId, String candidateId);

    Optional<Application> findByJobIdAndCandidateId(String jobId, String candidateId);
}
