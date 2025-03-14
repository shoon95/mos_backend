package com.mos.backend.studyjoins.infrastructure;

import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studyjoins.entity.StudyJoinStatus;

import java.util.List;
import java.util.Optional;

public interface StudyJoinRepository {
    Optional<StudyJoin> findById(Long studyApplicationId);
    List<StudyJoin> findAllByStatusWithStudy(StudyJoinStatus status);
}
