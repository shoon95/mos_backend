package com.mos.backend.studynotices.infrastructure;

import com.mos.backend.studynotices.entity.StudyNotice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyNoticeJpaRepository extends JpaRepository<StudyNotice, Long> {
    @EntityGraph(attributePaths = {"user", "study"})
    List<StudyNotice> findAllByStudyId(Long studyId);

    Optional<StudyNotice> findByStudyIdAndImportantIsTrue(Long studyId);

    @EntityGraph(attributePaths = {"user", "study"})
    Optional<StudyNotice> findById(Long studyNoticeId);
}
