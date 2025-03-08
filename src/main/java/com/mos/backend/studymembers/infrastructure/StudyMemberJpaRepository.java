package com.mos.backend.studymembers.infrastructure;

import com.mos.backend.studymembers.entity.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyMemberJpaRepository extends JpaRepository<StudyMember, Long> {
}