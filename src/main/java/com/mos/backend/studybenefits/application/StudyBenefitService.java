package com.mos.backend.studybenefits.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studybenefits.application.responsedto.StudyBenefitResponseDto;
import com.mos.backend.studybenefits.entity.StudyBenefit;
import com.mos.backend.studybenefits.entity.exception.StudyBenefitErrorCode;
import com.mos.backend.studybenefits.infrastructure.StudyBenefitRepository;
import com.mos.backend.studybenefits.presentation.requestdto.StudyBenefitRequestDto;
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
public class StudyBenefitService {

    private final StudyBenefitRepository studyBenefitRepository;
    private final EntityFacade entityFacade;

    /**
     * StudyBenefit 생성 | 수정 | 삭제
     */
    @Transactional
    public List<StudyBenefitResponseDto> createOrUpdateOrDelete(Long studyId, List<StudyBenefitRequestDto> benefitRequestDtoList) {
        validateRequest(benefitRequestDtoList);

        Study study = entityFacade.getStudy(studyId);
        List<StudyBenefit> benefitList = studyBenefitRepository.findAllByStudy(study);

        // 요청에 없는 기존 항목은 삭제
        List<StudyBenefit> deleteBenefitList = getDeleteBefitList(benefitRequestDtoList, benefitList);
        if (!deleteBenefitList.isEmpty()) {
            studyBenefitRepository.deleteAll(deleteBenefitList);
        }

        // id가 없으면 새로 저장, 있으면 업데이트
        benefitRequestDtoList.forEach(benefitRequestDto -> {
            if (benefitRequestDto.getId() == null) {
                studyBenefitRepository.save(StudyBenefit.create(study, benefitRequestDto.getBenefitNum(), benefitRequestDto.getContent()));
            } else {
                StudyBenefit studyBenefit = findByIdAndStudy(benefitRequestDto.getId(), study);
                studyBenefit.changeNumAndContent(benefitRequestDto.getBenefitNum(), benefitRequestDto.getContent());
            }
        });
        return getAll(studyId);
    }

    /**
     * StudyBenefit 목록 조회
     */
    public List<StudyBenefitResponseDto> getAll(Long studyId) {
        Study study = entityFacade.getStudy(studyId);
        List<StudyBenefit> studyBenefitList = studyBenefitRepository.findAllByStudy(study);
        studyBenefitList.sort(Comparator.comparing(StudyBenefit::getBenefitNum));
        return studyBenefitList.stream().map(StudyBenefitResponseDto::from).toList();
    }

    private static List<StudyBenefit> getDeleteBefitList(List<StudyBenefitRequestDto> benefitRequestDtoList, List<StudyBenefit> benefitList) {
        List<Long> requestBenefitIds = benefitRequestDtoList.stream()
                .map(StudyBenefitRequestDto::getId)
                .filter(Objects::nonNull)
                .toList();
        return benefitList.stream()
                .filter(b -> !requestBenefitIds.contains(b.getId()))
                .toList();
    }

    private void validateRequest(List<StudyBenefitRequestDto> benefitRequestDtoList) {
        validateBenefitNum(benefitRequestDtoList);
    }

    private void validateBenefitNum(List<StudyBenefitRequestDto> benefitRequestDtoList) {
        Set<Long> benefitNumSet = benefitRequestDtoList.stream()
                .map(r -> r.getBenefitNum())
                .collect(Collectors.toSet());

        if (benefitNumSet.size() != benefitRequestDtoList.size()) {
            throw new MosException(StudyBenefitErrorCode.INVALID_BENEFIT_NUM);
        }
    }

    private StudyBenefit findByIdAndStudy(Long benefitId, Study study) {
        return studyBenefitRepository.findByIdAndStudy(benefitId, study).orElseThrow(()-> new MosException(StudyBenefitErrorCode.STUDY_BENEFIT_NOT_FOUND));
    }
}
