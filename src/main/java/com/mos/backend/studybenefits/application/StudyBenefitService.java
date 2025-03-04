package com.mos.backend.studybenefits.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studybenefits.entity.StudyBenefit;
import com.mos.backend.studybenefits.infrastructure.StudyBenefitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyBenefitService {

    private final StudyBenefitRepository studyBenefitRepository;
    private final StudyRepository studyRepository;

    public void create(Long studyId, List<String> contents) {
        if (contents.isEmpty()) {
            return;
        }

        Study study = studyRepository.findById(studyId).orElseThrow(() -> new MosException(StudyErrorCode.STUDY_NOT_FOUND));
        List<StudyBenefit> studyBenefitList = contents.stream().map(c -> StudyBenefit.create(study, c)).toList();
        studyBenefitRepository.saveAll(studyBenefitList);
    }
}
