package com.mos.backend.studynotices.infrastructure;

import com.mos.backend.studynotices.entity.StudyNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudyNoticeJpaRepository extends JpaRepository<StudyNotice, Long> {
    @Query("SELECT sn FROM StudyNotice sn Join FETCH sn.user WHERE sn.study.id = :studyId")
    List<StudyNotice> findAllByStudyIdWithUser(Long studyId);

    Optional<StudyNotice> findByStudyIdAndImportantIsTrue(Long studyId);

    @Query("SELECT sn FROM StudyNotice sn JOIN FETCH sn.user WHERE sn.id =  :studyNoticeId")
    Optional<StudyNotice> findByIdWithUser(Long studyNoticeId);
}
