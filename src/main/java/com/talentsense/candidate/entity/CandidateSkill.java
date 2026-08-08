package com.talentsense.candidate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "candidate_skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateSkill {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateProfile candidate;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    @Builder.Default
    private String category = "TECHNICAL";

    @Column(length = 50)
    @Builder.Default
    private String proficiency = "INTERMEDIATE";

    @Column(name = "years_experience")
    @Builder.Default
    private Integer yearsExperience = 1;

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
