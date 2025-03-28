package com.mos.backend.studymembers.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studymembers.entity.ParticipationStatus;
import com.mos.backend.studymembers.entity.StudyMember;
import com.mos.backend.studymembers.entity.StudyMemberRoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyMemberJpaRepository extends JpaRepository<StudyMember, Long> {
    int countByStudyAndStatusIn(Study study, List<ParticipationStatus> statusList);

    List<StudyMember> findAllByStudyId(Long studyId);

    Optional<StudyMember> findByUserIdAndStudyId(Long userId, Long studyId);

    boolean existsByStudyAndRoleType(Study study, StudyMemberRoleType roleType);
}
