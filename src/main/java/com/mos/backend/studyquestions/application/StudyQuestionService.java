package com.mos.backend.studyquestions.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studyquestions.presentation.requestdto.StudyQuestionCreateRequestDto;
import com.mos.backend.studyquestions.entity.QuestionOption;
import com.mos.backend.studyquestions.entity.QuestionType;
import com.mos.backend.studyquestions.entity.StudyQuestion;
import com.mos.backend.studyquestions.infrastructure.StudyQuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyQuestionService {

    private final StudyQuestionRepository studyQuestionRepository;
    private final StudyRepository studyRepository;

    @Transactional
    public void create(Long studyId, List<StudyQuestionCreateRequestDto> studyQuestionCreateRequestDtoList) {
        if (studyQuestionCreateRequestDtoList.isEmpty()) {
            return;
        }

        Study study = studyRepository.findById(studyId).orElseThrow(() -> new MosException(StudyErrorCode.STUDY_NOT_FOUND));

        List<StudyQuestion> studyQuestionList = studyQuestionCreateRequestDtoList.stream().map(q ->
                StudyQuestion.create(study, q.getQuestionId(), q.getQuestion(), QuestionType.fromDescription(q.getType()), QuestionOption.fromList(q.getOptions()), q.isRequired())).toList();
        studyQuestionRepository.saveAll(studyQuestionList);
    }
}
