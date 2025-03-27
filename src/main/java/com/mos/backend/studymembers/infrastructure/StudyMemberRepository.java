package com.mos.backend.studymembers.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studymembers.entity.ParticipationStatus;
import com.mos.backend.studymembers.entity.StudyMember;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository {

    StudyMember save(StudyMember studyMember);

    int countByStudyAndStatusIn(Study study, List<ParticipationStatus> statusList);

    Optional<StudyMember> findById(Long studyMemberId);

    List<StudyMember> findAllByStudyId(Long studyId);

    Optional<StudyMember> findByUserIdAndStudyId(Long userId, Long studyId);
}
