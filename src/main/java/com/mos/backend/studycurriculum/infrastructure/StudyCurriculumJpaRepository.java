package com.mos.backend.studycurriculum.infrastructure;

import com.mos.backend.studies.entity.Study;
import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudyCurriculumJpaRepository extends JpaRepository<StudyCurriculum, Long> {
    List<StudyCurriculum> findAllByStudy(Study study);

    Optional<StudyCurriculum> findByIdAndStudy(Long id, Study study);

    @Query("""
            SELECT sc 
            FROM StudyCurriculum sc 
            JOIN StudyScheduleCurriculum ssc ON sc.id = ssc.studyCurriculum.id
            WHERE ssc.studySchedule.id = :studyScheduleId
            """)
    List<StudyCurriculum> findAllByStudyScheduleId(Long studyScheduleId);
}