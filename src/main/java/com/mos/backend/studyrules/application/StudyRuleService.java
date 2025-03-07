package com.mos.backend.studyrules.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studyrules.entity.StudyRule;
import com.mos.backend.studyrules.infrastructure.StudyRuleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyRuleService {

    private final StudyRuleRepository studyRuleRepository;
    private final StudyRepository studyRepository;

    public void create(Long studyId, List<String> contents) {
        if (contents.isEmpty()) {
            return;
        }

        Study study = getStudyById(studyId);

        List<StudyRule> studyRuleList = contents.stream().map(c -> StudyRule.create(study, c)).toList();

        studyRuleRepository.saveAll(studyRuleList);
    }

    private Study getStudyById(Long studyId) {
        return studyRepository.findById(studyId).orElseThrow(() -> new MosException(StudyErrorCode.STUDY_NOT_FOUND));
    }
}
