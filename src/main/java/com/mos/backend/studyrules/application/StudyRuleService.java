package com.mos.backend.studyrules.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyrules.application.responsedto.StudyRuleResponseDto;
import com.mos.backend.studyrules.entity.StudyRule;
import com.mos.backend.studyrules.entity.exception.StudyRuleErrorCode;
import com.mos.backend.studyrules.infrastructure.StudyRuleRepository;
import com.mos.backend.studyrules.presentation.requestdto.StudyRuleCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyRuleService {

    private final StudyRuleRepository studyRuleRepository;
    private final EntityFacade entityFacade;

    /**
     * studyRule 생성 | 수정 | 삭제
     */

    @Transactional
    public List<StudyRuleResponseDto> createOrUpdateOrDelete(Long studyId, List<StudyRuleCreateRequestDto> requestDtoList) {
        validateRequest(requestDtoList);
        Study study = entityFacade.getStudy(studyId);

        List<StudyRule> studyRuleList = studyRuleRepository.findAllByStudy(study);

        // 요청에 없는 기존 항목은 삭제
        List<StudyRule> deleteRuleList = getDeleteStudyRuleList(requestDtoList, studyRuleList);
        if (!deleteRuleList.isEmpty()) {
            studyRuleRepository.deleteAll(deleteRuleList);
        }

        // id가 없으면 새로 저장, 있으면 업데이트
        requestDtoList.forEach(requestDto -> {
            if (requestDto.getId() == null) {
                studyRuleRepository.save(StudyRule.create(study, requestDto.getRuleNum(), requestDto.getContent()));
            } else {
                StudyRule studyRule = findByIdAndStudy(requestDto.getId(), study);
                studyRule.update(requestDto.getRuleNum(), requestDto.getContent());
            }
        });
        return getAll(studyId);
    }

    /**
     * StudyCurriculum 목록 조회
     */

    public List<StudyRuleResponseDto> getAll(Long studyId) {
        Study study = entityFacade.getStudy(studyId);
        List<StudyRule> studyRuleList = studyRuleRepository.findAllByStudy(study);
        studyRuleList.sort(Comparator.comparing(StudyRule::getRuleNum));
        return studyRuleList.stream().map(StudyRuleResponseDto::from).toList();
    }

    /**
     * StudyCurriculum 단 건 조회
     */

    public StudyRuleResponseDto get(Long studyId, Long studyRuleId) {
        Study study = entityFacade.getStudy(studyId);
        StudyRule studyRule = findByIdAndStudy(studyRuleId, study);
        return StudyRuleResponseDto.from(studyRule);
    }


    private void validateRequest(List<StudyRuleCreateRequestDto> requestDtoList) {
        validateRuleNum(requestDtoList);
    }

    private void validateRuleNum(List<StudyRuleCreateRequestDto> requestDtoList) {
        Set<Long> ruleNumSet = requestDtoList.stream()
                .map(StudyRuleCreateRequestDto::getRuleNum)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // null 이 있는지 확인
        if (ruleNumSet.size() != requestDtoList.size()) {
            throw new MosException(StudyRuleErrorCode.INVALID_RULE_NUM);
        }

        long maxRuleNum = ruleNumSet.stream()
                .max(Long::compareTo)
                .orElse(0L); // 값이 없으면 0으로 처리

        long expectedSum = (maxRuleNum * (maxRuleNum + 1)) / 2;
        long actualSum = ruleNumSet.stream().mapToLong(Long::longValue).sum();

        // 1부터 연속적인 수인지 검증
        if (expectedSum != actualSum) {
            throw new MosException(StudyRuleErrorCode.INVALID_RULE_NUM);
        }
    }


    private List<StudyRule> getDeleteStudyRuleList(List<StudyRuleCreateRequestDto> requestDtoList, List<StudyRule> studyRuleList) {
        List<Long> requestStudyRuleIdList = requestDtoList.stream()
                .map(StudyRuleCreateRequestDto::getId)
                .filter(Objects::nonNull)
                .toList();
        return studyRuleList.stream()
                .filter(b -> !requestStudyRuleIdList.contains(b.getId()))
                .toList();

    }

    private StudyRule findByIdAndStudy(Long id, Study study) {
        return studyRuleRepository.findByIdAndStudy(id, study).orElseThrow(() -> new MosException(StudyRuleErrorCode.STUDY_RULE_NOT_FOUND));
    }

}
