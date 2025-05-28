package com.mos.backend.studies.application;

import com.mos.backend.common.event.Event;
import com.mos.backend.common.event.EventType;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.common.utils.ClientInfoExtractor;
import com.mos.backend.common.utils.RandomColorGenerator;
import com.mos.backend.hotstudies.entity.HotStudyEventType;
import com.mos.backend.hotstudies.infrastructure.HotStudyRepository;
import com.mos.backend.studies.application.event.StudyCreatedEventPayload;
import com.mos.backend.studies.application.event.StudyDeletedEventPayload;
import com.mos.backend.studies.application.event.StudyViewedEventPayload;
import com.mos.backend.studies.application.responsedto.*;
import com.mos.backend.studies.entity.*;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studies.infrastructure.StudyRepository;
import com.mos.backend.studies.presentation.requestdto.StudyCreateRequestDto;
import com.mos.backend.studymembers.application.StudyMemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyMemberService studyMemberService;
    private final HotStudyRepository hotStudyRepository;
    private final EntityFacade entityFacade;
    private final ViewCountService viewCountService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 스터디 생성
     */

    @Transactional
    public StudyCreateResponseDto create(Long userId, StudyCreateRequestDto requestDto) {
        validateStudyCreateRequest(requestDto);

        Study study = convertToEntity(requestDto);
        Study savedStudy = studyRepository.save(study);

        eventPublisher.publishEvent(new Event<>(EventType.STUDY_CREATED, new StudyCreatedEventPayload(userId, requestDto, savedStudy.getId())));
        return new StudyCreateResponseDto(savedStudy.getId());
    }

    /**
     * 스터디 단 건 조회
     */

    @Transactional
    public StudyResponseDto get(long studyId, HttpServletRequest httpServletRequest) {
        String ipAddress = ClientInfoExtractor.extractIpAddress(httpServletRequest);

        viewCountService.handleViewCount(studyId, ipAddress);

        Study study = findStudyById(studyId);
        int studyMemberCount = studyMemberService.countCurrentStudyMember(studyId);

        eventPublisher.publishEvent(new Event<>(EventType.STUDY_VIEWED, new StudyViewedEventPayload(HotStudyEventType.VIEW, studyId)));

        return StudyResponseDto.from(study, studyMemberCount);
    }

    /**
     * 스터디 다 건 조회
     */

    public StudyCardListResponseDto findStudies(Pageable pageable, String categoryCond, String meetingTypeCond, String recruitmentStatusCond, String progressStatusCond) {
        Page<StudiesResponseDto> studies = studyRepository.findStudies(pageable, categoryCond, meetingTypeCond, recruitmentStatusCond, progressStatusCond);
        return new StudyCardListResponseDto(studies.getTotalElements(), studies.getNumber(), studies.getTotalPages(), studies.getContent());
    }

    /**
     * 인기 스터디 조회
     * @return
     */
    @Transactional
    public List<StudiesResponseDto> readHotStudies() {
        return hotStudyRepository.readAll().stream()
                .map(id -> {
                    try {
                        return this.getHotStudy(id);
                    } catch (MosException e) {
                        hotStudyRepository.remove(id);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Study 삭제
     */

    @Transactional
    public void delete(Long userId, Long studyId) {
        Study study = entityFacade.getStudy(studyId);
        studyRepository.delete(study);
        eventPublisher.publishEvent(new Event<>(EventType.STUDY_DELETED, new StudyDeletedEventPayload(HotStudyEventType.DELETE, userId, studyId)));
    }

    @Transactional
    public void changeImageToPermanent(Long userId, Long studyId) {
        Study study = entityFacade.getStudy(studyId);
        study.changeImageToPermanent(userId, studyId);
    }

    /**
     * 인기 스터디 목록 조회
     */
    public StudiesResponseDto getHotStudy(Long studyId) {
        Study study = entityFacade.getStudy(studyId);
        int currentStudyMembers = studyMemberService.countCurrentStudyMember(studyId);
        return StudiesResponseDto.from(study, Long.valueOf(currentStudyMembers));
    }

    /**
     * 스터디 카테고리 조회
     */
    public StudyCategoriesResponseDto getStudyCategories() {
        List<String> list = Arrays.stream(Category.values()).map(Category::getDescription).toList();
        return new StudyCategoriesResponseDto(list);
    }

    private Study findStudyById(long studyId) {
        return studyRepository.findById(studyId).orElseThrow(() -> new MosException(StudyErrorCode.STUDY_NOT_FOUND));
    }


    private static Study convertToEntity(StudyCreateRequestDto requestDto) {
        return Study.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .maxStudyMemberCount(requestDto.getMaxStudyMemberCount())
                .category(Category.fromDescription(requestDto.getCategory()))
                .schedule(requestDto.getSchedule())
                .recruitmentStartDate(requestDto.getRecruitmentStartDate())
                .recruitmentEndDate(requestDto.getRecruitmentEndDate())
                .tags(StudyTag.fromList(requestDto.getTags()))
                .color(RandomColorGenerator.generateRandomColor())
                .meetingType(MeetingType.fromDescription(requestDto.getMeetingType()))
                .build();
    }

    private void validateStudyCreateRequest(StudyCreateRequestDto requestDto) {
        if (requestDto.getRecruitmentStartDate().isAfter(requestDto.getRecruitmentEndDate())) {
            throw new MosException(StudyErrorCode.INVALID_RECRUITMENT_DATES);
        }
    }

    public void validateRecruitmentPeriod(Study study) {
        LocalDate now = LocalDate.now();
        if (!(study.getRecruitmentStartDate().isBefore(now) && study.getRecruitmentEndDate().isAfter(now)))
            throw new MosException(StudyErrorCode.NOT_IN_RECRUITMENT_PERIOD);
    }

    public void validateRecruitmentStatus(Study study) {
        if (study.getRecruitmentStatus().equals(RecruitmentStatus.CLOSED))
            throw new MosException(StudyErrorCode.RECRUITMENT_CLOSED);
    }
}
