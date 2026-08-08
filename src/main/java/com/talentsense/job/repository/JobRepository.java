package com.talentsense.job.repository;

import com.talentsense.job.entity.Job;
import com.talentsense.job.entity.Job.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {

    Page<Job> findByStatus(JobStatus status, Pageable pageable);

    List<Job> findByOrganizationId(String organizationId);

    List<Job> findByCreatedByUserId(String userId);

    @Query("SELECT j FROM Job j WHERE j.status = 'PUBLISHED' AND " +
           "(:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:industry IS NULL OR LOWER(j.industry) LIKE LOWER(CONCAT('%', :industry, '%')))")
    Page<Job> searchJobs(@Param("keyword") String keyword,
                         @Param("location") String location,
                         @Param("industry") String industry,
                         Pageable pageable);
}
