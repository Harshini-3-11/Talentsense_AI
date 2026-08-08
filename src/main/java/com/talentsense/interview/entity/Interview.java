package com.talentsense.interview.entity;

import com.talentsense.application.entity.Application;
import com.talentsense.candidate.entity.CandidateProfile;
import com.talentsense.job.entity.Job;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateProfile candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(name = "interview_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private InterviewType interviewType;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private InterviewStatus status = InterviewStatus.SCHEDULED;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "duration_minutes")
    @Builder.Default
    private Integer durationMinutes = 45;

    @Column(name = "feedback_summary", columnDefinition = "TEXT")
    private String feedbackSummary;

    @Column(name = "overall_score")
    private Integer overallScore;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public enum InterviewType {
        TECHNICAL, BEHAVIORAL, MOCK, HR, SYSTEM_DESIGN
    }

    public enum InterviewStatus {
        SCHEDULED, COMPLETED, CANCELLED
    }
}
