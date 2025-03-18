package com.mos.backend.studyschedules.infrastructure;

import com.mos.backend.studyschedules.entity.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyScheduleJpaRepository extends JpaRepository<StudySchedule, Long> {
    List<StudySchedule> findByStudyId(Long studyId);

    @Query("""
                SELECT ss 
                FROM StudySchedule ss
                JOIN ss.study s
                JOIN StudyMember sm ON sm.study = s
                WHERE sm.user.id = :userId AND sm.status = 'ACTIVATED'
            """)
    List<StudySchedule> findAllByActivatedUserId(Long userId);
}
