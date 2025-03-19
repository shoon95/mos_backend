package com.mos.backend.studycurriculum.infrastructure;

import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyCurriculumJpaRepository extends JpaRepository<StudyCurriculum, Long> {

    @Query("""
            SELECT sc 
            FROM StudyCurriculum sc 
            JOIN StudyScheduleCurriculum ssc ON sc.id = ssc.studyCurriculum.id
            WHERE ssc.studySchedule.id = :studyScheduleId
            """)
    List<StudyCurriculum> findAllByStudyScheduleId(Long studyScheduleId);
}
