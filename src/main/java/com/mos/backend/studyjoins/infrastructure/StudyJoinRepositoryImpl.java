package com.mos.backend.studyjoins.infrastructure;

import com.mos.backend.studyjoins.entity.StudyJoin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyJoinRepositoryImpl implements StudyJoinRepository {
    private final StudyJoinJpaRepository studyJoinJpaRepository;

    @Override
    public Optional<StudyJoin> findById(Long studyApplicationId) {
        return studyJoinJpaRepository.findById(studyApplicationId);
    }
}
