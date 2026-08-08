package com.talentsense.interview.repository;

import com.talentsense.interview.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, String> {

    List<Interview> findByCandidateId(String candidateId);

    List<Interview> findByJobId(String jobId);
}
