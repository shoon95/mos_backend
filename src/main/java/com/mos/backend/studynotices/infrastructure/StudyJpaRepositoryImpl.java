package com.mos.backend.studynotices.infrastructure;

import com.mos.backend.studynotices.entity.StudyNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyJpaRepositoryImpl implements StudyNoticeRepository{

    private final StudyNoticeJpaRepository studyNoticeJpaRepository;


    @Override
    public StudyNotice save(StudyNotice studyNotice) {
        return studyNoticeJpaRepository.save(studyNotice);
    }

    @Override
    public List<StudyNotice> findAllByStudyIdWithUser(Long studyId) {
        return studyNoticeJpaRepository.findAllByStudyIdWithUser(studyId);
    }

    @Override
    public Optional<StudyNotice> findByStudyIdAndImportantIsTrue(Long studyId) {
        return studyNoticeJpaRepository.findByStudyIdAndImportantIsTrue(studyId);
    }

    @Override
    public Optional<StudyNotice> findByIdWithUser(Long studyNoticeId) {
        return studyNoticeJpaRepository.findByIdWithUser(studyNoticeId);
    }

    @Override
    public void deleteById(Long studyNoticeId) {
        studyNoticeJpaRepository.deleteById(studyNoticeId);
    }
}
