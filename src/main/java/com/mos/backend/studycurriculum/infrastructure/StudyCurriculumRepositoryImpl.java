package com.mos.backend.studycurriculum.infrastructure;

import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyCurriculumRepositoryImpl implements StudyCurriculumRepository {

    private final StudyCurriculumJpaRepository studyCurriculumJpaRepository;

    @Override
    public StudyCurriculum save(StudyCurriculum studyCurriculum) {
        return studyCurriculumJpaRepository.save(studyCurriculum);
    }

    @Override
    public void saveAll(List<StudyCurriculum> studyCurriculumList) {
        studyCurriculumJpaRepository.saveAll(studyCurriculumList);
    }

    @Override
    public Optional<StudyCurriculum> findById(Long studyCurriculumId) {
        return studyCurriculumJpaRepository.findById(studyCurriculumId);
    }

    @Override
    public List<StudyCurriculum> findAllByStudyScheduleId(Long studyScheduleId) {
        return studyCurriculumJpaRepository.findAllByStudyScheduleId(studyScheduleId);
    }
}
