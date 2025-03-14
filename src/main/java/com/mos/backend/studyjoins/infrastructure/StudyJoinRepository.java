package com.mos.backend.studyjoins.infrastructure;

import com.mos.backend.studyjoins.entity.StudyJoin;

import java.util.Optional;

public interface StudyJoinRepository {
    Optional<StudyJoin> findById(Long studyApplicationId);
}
