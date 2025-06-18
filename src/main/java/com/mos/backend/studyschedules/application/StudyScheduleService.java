package com.mos.backend.studyschedules.application;

import com.amazonaws.util.CollectionUtils;
import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studycurriculum.infrastructure.StudyCurriculumRepository;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final EntityFacade entityFacade;

    @PreAuthorize("@studySecurity.isLeaderOrAdmin(#studyId)")
    @Transactional
    public void createStudySchedule(Long studyId, StudyScheduleCreateReq req) {
        Study study = entityFacade.getStudy(studyId);

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

    @PreAuthorize("@userSecurity.isOwnerOrAdmin(#userId)")
    @Transactional(readOnly = true)
    public List<StudyScheduleRes> getMyStudySchedules(Long userId) {
        User user = entityFacade.getUser(userId);

        List<StudySchedule> studySchedules = studyScheduleRepository.findAllByActivatedUserId(user.getId());
        return convertToRes(studySchedules);
    }

    @PreAuthorize("@studySecurity.isMemberOrAdmin(#studyId)")
    @Transactional(readOnly = true)
    public List<StudyScheduleRes> getStudySchedules(Long studyId) {
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


    @PreAuthorize("@studySecurity.isLeaderOrAdmin(#studyId)")
    @Transactional
    public void updateStudySchedule(Long studyId, Long studyScheduleId, StudyScheduleUpdateReq req) {
        Study study = entityFacade.getStudy(studyId);
        StudySchedule studySchedule = entityFacade.getStudySchedule(studyScheduleId);

        validateEndDateTime(req.getStartDateTime(), req.getEndDateTime());

        studyScheduleCurriculumService.deleteAll(study.getId(), studySchedule.getId());

        List<Long> curriculumIds = req.getCurriculumIds();
        if (!CollectionUtils.isNullOrEmpty(curriculumIds)) {
            studyScheduleCurriculumService.saveAll(study.getId(), studySchedule.getId(), curriculumIds);
        }

        studySchedule.update(req.getTitle(), req.getDescription(), req.getStartDateTime(), req.getEndDateTime());
    }

    @PreAuthorize("@studySecurity.isLeaderOrAdmin(#studyId)")
    @Transactional
    public void deleteStudySchedule(Long studyId, Long studyScheduleId) {
        StudySchedule studySchedule = entityFacade.getStudySchedule(studyScheduleId);

        studyScheduleRepository.delete(studySchedule);
    }

    private StudySchedule saveStudySchedule(StudyScheduleCreateReq req, Study study) {
        StudySchedule studySchedule = StudySchedule.create(
                study, req.getTitle(), req.getDescription(), req.getStartDateTime(), req.getEndDateTime()
        );

        return studyScheduleRepository.save(studySchedule);
    }

}
