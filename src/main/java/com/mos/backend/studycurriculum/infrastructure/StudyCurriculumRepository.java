package com.mos.backend.studycurriculum.infrastructure;

import com.mos.backend.studycurriculum.entity.StudyCurriculum;

import java.util.List;

public interface StudyCurriculumRepository {
    void saveAll(List<StudyCurriculum> studyCurriculumList);
}
