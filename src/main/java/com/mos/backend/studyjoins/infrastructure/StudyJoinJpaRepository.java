package com.mos.backend.studyjoins.infrastructure;

import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studyjoins.entity.StudyJoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyJoinJpaRepository extends JpaRepository<StudyJoin, Long> {
    @Query("""
            SELECT sj 
            FROM StudyJoin sj 
            JOIN FETCH sj.study 
            WHERE sj.status = :studyJoinStatus
            """)
    List<StudyJoin> findAllByStatusWithStudy(@Param("studyJoinStatus") StudyJoinStatus studyJoinStatus);

    @Query("""
                SELECT sj
                FROM StudyJoin sj 
                JOIN FETCH sj.user
                WHERE sj.study.id = :studyId
            """)
    List<StudyJoin> findAllByStudyId(@Param("studyId") Long studyId);
}
