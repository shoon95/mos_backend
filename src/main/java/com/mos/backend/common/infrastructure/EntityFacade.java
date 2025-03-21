package com.mos.backend.common.infrastructure;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studybenefits.entity.StudyBenefit;
import com.mos.backend.studybenefits.entity.exception.StudyBenefitErrorCode;
import com.mos.backend.studybenefits.infrastructure.StudyBenefitRepository;
import com.mos.backend.studyjoins.entity.StudyJoin;
import com.mos.backend.studyjoins.entity.exception.StudyJoinErrorCode;
import com.mos.backend.studyjoins.infrastructure.StudyJoinRepository;
import com.mos.backend.studyquestions.entity.StudyQuestion;
import com.mos.backend.studyquestions.entity.StudyQuestionErrorCode;
import com.mos.backend.studyquestions.infrastructure.StudyQuestionRepository;
import com.mos.backend.users.entity.User;
import com.mos.backend.users.entity.exception.UserErrorCode;
import com.mos.backend.users.infrastructure.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class EntityFacade {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final StudyJoinRepository studyJoinRepository;
    private final StudyBenefitRepository studyBenefitRepository;
    private final StudyQuestionRepository studyQuestionRepository;

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new MosException(UserErrorCode.USER_NOT_FOUND));
    }

    public Study getStudy(Long studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(() -> new MosException(StudyErrorCode.STUDY_NOT_FOUND));
    }

    public StudyJoin getStudyJoin(Long studyApplicationId) {
        return studyJoinRepository.findById(studyApplicationId)
                .orElseThrow(() -> new MosException(StudyJoinErrorCode.STUDY_JOIN_NOT_FOUND));
    }

    public StudyBenefit getStudyBenefit(Long studyBenefitId) {
        return studyBenefitRepository.findById(studyBenefitId)
                .orElseThrow(() -> new MosException(StudyBenefitErrorCode.STUDY_BENEFIT_NOT_FOUND));
    }

    public StudyQuestion getStudyQuestion(Long studyQuestionId) {
        return studyQuestionRepository.findById(studyQuestionId)
                .orElseThrow(() -> new MosException(StudyQuestionErrorCode.STUDY_QUESTION_NOT_FOUND));
    }
}
