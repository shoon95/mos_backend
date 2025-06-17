package com.mos.backend.studynotices.infrastructure;

import com.mos.backend.studynotices.entity.StudyNotice;

import java.util.List;
import java.util.Optional;

public interface StudyNoticeRepository {

    StudyNotice save(StudyNotice studyNotice);

    List<StudyNotice> findAllByStudyIdWithUser(Long studyId);

    Optional<StudyNotice> findByStudyIdAndImportantIsTrue(Long studyId);

    Optional<StudyNotice> findByIdWithUser(Long studyNoticeId);

    void deleteById(Long studyNoticeId);
}
