package com.mos.backend.studyjoins.infrastructure;

import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studyjoins.entity.StudyJoinStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyJoinRepositoryImpl implements StudyJoinRepository {
    private final StudyJoinJpaRepository studyJoinJpaRepository;
    private final StudyJoinQueryDslRepository studyJoinQueryDslRepository;

    @Override
    public StudyJoin save(StudyJoin studyJoin) {
        return studyJoinJpaRepository.save(studyJoin);
    }

    @Override
    public Optional<StudyJoin> findById(Long studyApplicationId) {
        return studyJoinJpaRepository.findById(studyApplicationId);
    }

    @Override
    public List<StudyJoin> findAllByUserIdAndStatus(Long userId, StudyJoinStatus status) {
        return studyJoinQueryDslRepository.findAllByUserIdAndStatus(userId, status);
    }

    @Override
    public List<StudyJoin> findAllByStudyId(Long studyId) {
        return studyJoinJpaRepository.findAllByStudyId(studyId);
    }
}
