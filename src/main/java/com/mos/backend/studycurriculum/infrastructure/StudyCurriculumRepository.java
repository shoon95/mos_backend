package com.mos.backend.studycurriculum.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studycurriculum.entity.StudyCurriculum;

import java.util.List;
import java.util.Optional;

public interface StudyCurriculumRepository {
    void saveAll(List<StudyCurriculum> studyCurriculumList);

    List<StudyCurriculum> findAllByStudy(Study study);

    void deleteAll(List<StudyCurriculum> studyCurriculumList);

    Optional<StudyCurriculum> findByIdAndStudy(Long id, Study study);

    Optional<StudyCurriculum > findById(Long studyCurriculumId);

    StudyCurriculum save(StudyCurriculum studyCurriculum);

    List<StudyCurriculum> findAllByStudyScheduleId(Long studyScheduleId);
}
