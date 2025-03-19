package com.mos.backend.studyschedulecurriculums.infrastructure;

import com.mos.backend.studyschedulecurriculums.entity.StudyScheduleCurriculum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyScheduleCurriculumJpaRepository  extends JpaRepository<StudyScheduleCurriculum, Long> {
}
