package com.mos.backend.studyschedules.application;

import com.amazonaws.util.CollectionUtils;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.application.StudyService;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studycurriculum.infrastructure.StudyCurriculumRepository;
import com.mos.backend.studymembers.application.StudyMemberService;
import com.mos.backend.studyschedulecurriculums.application.StudyScheduleCurriculumService;
import com.mos.backend.studyschedules.application.res.StudyCurriculumRes;
import com.mos.backend.studyschedules.application.res.StudyScheduleRes;
import com.mos.backend.studyschedules.entity.StudySchedule;
import com.mos.backend.studyschedules.entity.exception.StudyScheduleErrorCode;
import com.mos.backend.studyschedules.infrastructure.StudyScheduleRepository;
import com.mos.backend.studyschedules.presentation.req.StudyScheduleCreateReq;
import com.mos.backend.studyschedules.presentation.req.StudyScheduleUpdateReq;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class StudyScheduleService {
    private final StudyScheduleRepository studyScheduleRepository;
    private final StudyCurriculumRepository studyCurriculumRepository;
    private final StudyScheduleCurriculumService studyScheduleCurriculumService;
    private final StudyMemberService studyMemberService;
    private final StudyService studyService;
    private final EntityFacade entityFacade;

    @Transactional
    public void createStudySchedule(Long userId, Long studyId, StudyScheduleCreateReq req) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);

        studyService.validateRelation(study, study.getId());
        validateEndDateTime(req.getStartDateTime(), req.getEndDateTime());

        StudySchedule studySchedule = saveStudySchedule(req, study);

        List<Long> curriculumIds = req.getCurriculumIds();
        if (!CollectionUtils.isNullOrEmpty(curriculumIds))
            studyScheduleCurriculumService.saveAll(study.getId(), studySchedule.getId(), curriculumIds);
    }

    private static void validateEndDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (!Objects.isNull(endDateTime))
            if (!endDateTime.isAfter(startDateTime))
                throw new MosException(StudyScheduleErrorCode.INVALID_END_DATE_TIME);
    }

    @Transactional(readOnly = true)
    public List<StudyScheduleRes> getMyStudySchedules(Long userId) {
        User user = entityFacade.getUser(userId);

        List<StudySchedule> studySchedules = studyScheduleRepository.findAllByActivatedUserId(user.getId());
        return convertToRes(studySchedules);
    }

    @Transactional(readOnly = true)
    public List<StudyScheduleRes> getStudySchedules(Long userId, Long studyId) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);

        List<StudySchedule> studySchedules = studyScheduleRepository.findByStudyId(study.getId());
        return convertToRes(studySchedules);
    }

    private List<StudyScheduleRes> convertToRes(List<StudySchedule> studySchedules) {
        return studySchedules.stream().map(studySchedule -> {
            List<StudyCurriculumRes> studyCurriculumResList = studyCurriculumRepository.findAllByStudyScheduleId(studySchedule.getId()).stream()
                    .map(StudyCurriculumRes::from)
                    .toList();
            return StudyScheduleRes.of(studySchedule, studyCurriculumResList);
        }).toList();
    }


    @Transactional
    public void updateStudySchedule(Long userId, Long studyId, Long studyScheduleId, StudyScheduleUpdateReq req) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);
        StudySchedule studySchedule = entityFacade.getStudySchedule(studyScheduleId);

        studyService.validateRelation(study, studySchedule.getStudy().getId());
        studyMemberService.validateStudyMember(user, study);
        validateEndDateTime(req.getStartDateTime(), req.getEndDateTime());

        studyScheduleCurriculumService.deleteAll(study.getId(), studySchedule.getId());

        List<Long> curriculumIds = req.getCurriculumIds();
        if (!CollectionUtils.isNullOrEmpty(curriculumIds)) {
            studyScheduleCurriculumService.saveAll(study.getId(), studySchedule.getId(), curriculumIds);
        }

        studySchedule.update(req.getTitle(), req.getDescription(), req.getStartDateTime(), req.getEndDateTime());
    }

    @Transactional
    public void deleteStudySchedule(Long userId, Long studyId, Long studyScheduleId) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);
        StudySchedule studySchedule = entityFacade.getStudySchedule(studyScheduleId);

        studyMemberService.validateStudyMember(user, study);

        studyService.validateRelation(study, studySchedule.getStudy().getId());

        studyScheduleRepository.delete(studySchedule);
    }

    private StudySchedule saveStudySchedule(StudyScheduleCreateReq req, Study study) {
        StudySchedule studySchedule = StudySchedule.create(
                study, req.getTitle(), req.getDescription(), req.getStartDateTime(), req.getEndDateTime()
        );

        return studyScheduleRepository.save(studySchedule);
    }

}
