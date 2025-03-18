package com.mos.backend.studycurriculum.infrastructure;

import com.mos.backend.studycurriculum.entity.StudyCurriculum;

import java.util.List;
import java.util.Optional;

public interface StudyCurriculumRepository {
    void saveAll(List<StudyCurriculum> studyCurriculumList);

    Optional<StudyCurriculum > findById(Long studyCurriculumId);

    StudyCurriculum save(StudyCurriculum studyCurriculum);
}
