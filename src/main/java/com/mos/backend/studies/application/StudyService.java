package com.mos.backend.studies.application;

import com.mos.backend.studies.entity.Category;
import com.mos.backend.studies.entity.MeetingType;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.StudyTag;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studies.presentation.dto.StudyCreateRequestDto;
import com.mos.backend.studybenefits.application.StudyBenefitService;
import com.mos.backend.studycurriculum.application.StudyCurriculumService;
import com.mos.backend.studycurriculum.infrastructure.StudyCurriculumRepository;
import com.mos.backend.studymembers.application.StudyMemberService;
import com.mos.backend.studyquestions.application.StudyQuestionService;
import com.mos.backend.studyrules.application.StudyRuleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;

    private final StudyRuleService studyRuleService;
    private final StudyBenefitService studyBenefitService;
    private final StudyQuestionService studyQuestionService;
    private final StudyCurriculumService studyCurriculumService;
    private final StudyMemberService studyMemberService;

    public Long create(Long userId, StudyCreateRequestDto requestDto) {
        Study study = Study.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .notice(requestDto.getNotice())
                .maxParticipantsCount(requestDto.getMaxParticipants())
                .category(Category.fromDescription(requestDto.getCategory()))
                .schedule(requestDto.getSchedule())
                .recruitmentStartDate(requestDto.getRecruitmentStartDate())
                .recruitmentEndDate(requestDto.getRecruitmentEndDate())
                .tags(StudyTag.fromList(requestDto.getTags()))
                .color(RandomColorGenerator.generateRandomColor())
                .meetingType(MeetingType.fromDescription(requestDto.getMeetingType()))
                .requirements(requestDto.getRequirements())
                .build();
        Study savedStudy = studyRepository.save(study);

        // 스터디 생성 이벤트 발행

        studyRuleService.create(savedStudy.getId(), requestDto.getRules());
        studyBenefitService.create(savedStudy.getId(), requestDto.getBenefits());
        studyQuestionService.create(savedStudy.getId(), requestDto.getApplicationQuestions());
        studyCurriculumService.create(savedStudy.getId(), requestDto.getCurriculums());
        studyMemberService.create(savedStudy.getId(), userId);
        return savedStudy.getId();
    }

}
