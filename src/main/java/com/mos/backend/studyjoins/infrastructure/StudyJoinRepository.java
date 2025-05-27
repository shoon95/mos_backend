package com.mos.backend.studyjoins.infrastructure;

import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studyjoins.entity.StudyJoinStatus;

import java.util.List;
import java.util.Optional;

public interface StudyJoinRepository {
    StudyJoin save(StudyJoin studyJoin);

    Optional<StudyJoin> findById(Long studyApplicationId);

    List<StudyJoin> findAllByUserIdAndStatus(Long userId, StudyJoinStatus status);

    List<StudyJoin> findAllByStudyId(Long studyId);
}
