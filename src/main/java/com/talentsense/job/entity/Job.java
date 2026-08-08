package com.talentsense.job.entity;

import com.talentsense.organization.entity.Organization;
import com.talentsense.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdByUser;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 100)
    private String department;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String location;

    @Column(name = "remote_type", length = 50)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RemoteType remoteType = RemoteType.HYBRID;

    @Column(name = "employment_type", length = 50)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EmploymentType employmentType = EmploymentType.FULL_TIME;

    @Column(name = "salary_min", precision = 12, scale = 2)
    private BigDecimal salaryMin;

    @Column(name = "salary_max", precision = 12, scale = 2)
    private BigDecimal salaryMax;

    @Column(length = 10)
    @Builder.Default
    private String currency = "USD";

    @Column(name = "experience_min")
    @Builder.Default
    private Integer experienceMin = 0;

    @Column(name = "experience_max")
    private Integer experienceMax;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Seniority seniority = Seniority.MID;

    @Column(length = 100)
    private String industry;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private JobStatus status = JobStatus.DRAFT;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public enum RemoteType {
        REMOTE, HYBRID, ON_SITE
    }

    public enum EmploymentType {
        FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP
    }

    public enum Seniority {
        ENTRY, MID, SENIOR, LEAD, EXECUTIVE
    }

    public enum JobStatus {
        DRAFT, PUBLISHED, PAUSED, CLOSED
    }
}
