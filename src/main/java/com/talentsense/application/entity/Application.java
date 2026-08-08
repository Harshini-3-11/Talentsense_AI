package com.talentsense.application.entity;

import com.talentsense.candidate.entity.CandidateProfile;
import com.talentsense.job.entity.Job;
import com.talentsense.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "applications", uniqueConstraints = {
    @UniqueConstraint(name = "uk_job_candidate", columnNames = {"job_id", "candidate_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateProfile candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Column(name = "cover_letter", columnDefinition = "LONGTEXT")
    private String coverLetter;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(name = "match_score")
    @Builder.Default
    private Integer matchScore = 0;

    @Column(name = "match_analysis_json", columnDefinition = "LONGTEXT")
    private String matchAnalysisJson;

    @CreationTimestamp
    @Column(name = "applied_at", updatable = false)
    private LocalDateTime appliedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public enum ApplicationStatus {
        APPLIED, SCREENING, REVIEW, SHORTLISTED, INTERVIEW, OFFER, HIRED, REJECTED
    }
}
