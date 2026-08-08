package com.talentsense.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @Column(length = 36, nullable = false)
    private String id;

    @Column(nullable = false, unique = true, length = 50)
    private String name; // ROLE_CANDIDATE, ROLE_RECRUITER, ROLE_HIRING_MANAGER, ROLE_ADMIN

    @Column(length = 255)
    private String description;
}
