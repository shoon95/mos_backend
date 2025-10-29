package com.mos.backend.studymemberroles.entity;

import com.mos.backend.roles.entity.Role;
import com.mos.backend.studymembers.entity.StudyMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_member_roles")
public class StudyMemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_member_role_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_member_id", nullable = false)
    @OnDelete(action = CASCADE)
    private StudyMember studyMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
