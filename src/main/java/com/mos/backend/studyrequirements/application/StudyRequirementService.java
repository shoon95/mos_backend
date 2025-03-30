package com.mos.backend.studyrequirements.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyrequirements.application.responsedto.StudyRequirementResponseDto;
import com.mos.backend.studyrequirements.entity.StudyRequirement;
import com.mos.backend.studyrequirements.entity.exception.StudyRequirementErrorCode;
import com.mos.backend.studyrequirements.infrastructure.StudyRequirementRepository;
import com.mos.backend.studyrequirements.presentation.requestdto.StudyRequirementCreateRequestDto;
import com.mos.backend.studyrules.application.responsedto.StudyRuleResponseDto;
import com.mos.backend.studyrules.entity.StudyRule;
import com.mos.backend.studyrules.entity.exception.StudyRuleErrorCode;
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
public class StudyRequirementService {

    private final StudyRequirementRepository studyRequirementRepository;
    private final EntityFacade entityFacade;

    /**
     * StudyRequirement 생성 | 수정 | 삭제
     */
    @Transactional
    public List<StudyRequirementResponseDto> createOrUpdateOrDelete(Long studyId, List<StudyRequirementCreateRequestDto> requestDtoList) {
        validateRequest(requestDtoList);
        Study study = entityFacade.getStudy(studyId);

        List<StudyRequirement> studyRequirementList = studyRequirementRepository.findAllByStudy(study);

        // 요청에 없는 기존 항목은 삭제
        List<StudyRequirement> deleteRequirementList = getDeleteStudyRequirementList(requestDtoList, studyRequirementList);
        if (!deleteRequirementList.isEmpty()) {
            studyRequirementRepository.deleteAll(deleteRequirementList);
        }

        // id가 없으면 새로 저장, 있으면 업데이트
        requestDtoList.forEach(requestDto -> {
            if (requestDto.getId() == null) {
                studyRequirementRepository.save(StudyRequirement.create(study, requestDto.getRequirementNum(), requestDto.getContent()));
            } else {
                StudyRequirement studyRequirement = findByIdAndStudy(requestDto.getId(), study);
                studyRequirement.update(requestDto.getRequirementNum(), requestDto.getContent());
            }
        });
        return getAll(studyId);
    }

    /**
     * StudyRequirement 목록 조회
     */

    public List<StudyRequirementResponseDto> getAll(Long studyId) {
        Study study = entityFacade.getStudy(studyId);
        List<StudyRequirement> studyRequirementList = studyRequirementRepository.findAllByStudy(study);
        studyRequirementList.sort(Comparator.comparing(StudyRequirement::getRequirementNum));
        return studyRequirementList.stream().map(StudyRequirementResponseDto::from).toList();
    }

    /**
     * StudyRequirement 단 건 조회
     */

    public StudyRequirementResponseDto get(Long studyId, Long studyRequirementId) {
        Study study = entityFacade.getStudy(studyId);
        StudyRequirement studyRequirement = findByIdAndStudy(studyRequirementId, study);
        return StudyRequirementResponseDto.from(studyRequirement);
    }


    private void validateRequest(List<StudyRequirementCreateRequestDto> requestDtoList) {
        validateRequirementNum(requestDtoList);
    }

    private void validateRequirementNum(List<StudyRequirementCreateRequestDto> requestDtoList) {
        Set<Long> requirementNumSet = requestDtoList.stream()
                .map(StudyRequirementCreateRequestDto::getRequirementNum)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // null 이 있는지 확인
        if (requirementNumSet.size() != requestDtoList.size()) {
            throw new MosException(StudyRequirementErrorCode.INVALID_REQUIREMENT_NUM);
        }

        long maxRequirementNum = requirementNumSet.stream()
                .max(Long::compareTo)
                .orElse(0L); // 값이 없으면 0으로 처리

        long expectedSum = (maxRequirementNum * (maxRequirementNum + 1)) / 2;
        long actualSum = requirementNumSet.stream().mapToLong(Long::longValue).sum();

        // 1부터 연속적인 수인지 검증
        if (expectedSum != actualSum) {
            throw new MosException(StudyRequirementErrorCode.INVALID_REQUIREMENT_NUM);
        }
    }


    private List<StudyRequirement> getDeleteStudyRequirementList(List<StudyRequirementCreateRequestDto> requestDtoList, List<StudyRequirement> studyRequirementList) {
        List<Long> requestStudyRequirementIdList = requestDtoList.stream()
                .map(StudyRequirementCreateRequestDto::getId)
                .filter(Objects::nonNull)
                .toList();
        return studyRequirementList.stream()
                .filter(b -> !requestStudyRequirementIdList.contains(b.getId()))
                .toList();

    }

    private StudyRequirement findByIdAndStudy(Long id, Study study) {
        return studyRequirementRepository.findByIdAndStudy(id, study).orElseThrow(() -> new MosException(StudyRequirementErrorCode.STUDY_REQUIREMENT_NOT_FOUND));
    }
}
