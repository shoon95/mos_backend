package com.mos.backend.studyschedulecurriculums.infrastructure;

import com.mos.backend.studyschedulecurriculums.entity.StudyScheduleCurriculum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StudyScheduleCurriculumRepositoryImpl implements StudyScheduleCurriculumRepository {
    private final StudyScheduleCurriculumJpaRepository studyScheduleCurriculumJpaRepository;

    @Override
    public StudyScheduleCurriculum save(StudyScheduleCurriculum studyScheduleCurriculum) {
        return studyScheduleCurriculumJpaRepository.save(studyScheduleCurriculum);
    }
}
