package com.mos.backend.studynotices.infrastructure;

import com.mos.backend.studynotices.entity.StudyNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyNoticeRepositoryImpl implements StudyNoticeRepository{

    private final StudyNoticeJpaRepository studyNoticeJpaRepository;


    @Override
    public StudyNotice save(StudyNotice studyNotice) {
        return studyNoticeJpaRepository.save(studyNotice);
    }

    @Override
    public List<StudyNotice> findAllByStudyId(Long studyId) {
        return studyNoticeJpaRepository.findAllByStudyId(studyId);
    }

    @Override
    public Optional<StudyNotice> findByStudyIdAndImportantIsTrue(Long studyId) {
        return studyNoticeJpaRepository.findByStudyIdAndImportantIsTrue(studyId);
    }

    @Override
    public Optional<StudyNotice> findById(Long studyNoticeId) {
        return studyNoticeJpaRepository.findById(studyNoticeId);
    }

    @Override
    public void deleteById(Long studyNoticeId) {
        studyNoticeJpaRepository.deleteById(studyNoticeId);
    }
}
