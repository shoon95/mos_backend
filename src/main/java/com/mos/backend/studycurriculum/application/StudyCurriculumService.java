package com.mos.backend.studycurriculum.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import com.mos.backend.studycurriculum.infrastructure.StudyCurriculumRepository;
import com.mos.backend.studycurriculum.presentation.requestdto.StudyCurriculumCreateRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyCurriculumService {

    private final StudyCurriculumRepository studyCurriculumRepository;
    private final StudyRepository studyRepository;

    public void create(Long studyId, List<StudyCurriculumCreateRequestDto> requestDtoList) {

        if (requestDtoList.isEmpty()) {
            return;
        }

        Study study = studyRepository.findById(studyId).orElseThrow(() -> new MosException(StudyErrorCode.STUDY_NOT_FOUND));

        List<StudyCurriculum> studyCurriculumList = requestDtoList.stream().map(c ->
                StudyCurriculum.create(study, c.getTitle(), c.getSectionId(), c.getContent())).toList();

        studyCurriculumRepository.saveAll(studyCurriculumList);
    }
}
