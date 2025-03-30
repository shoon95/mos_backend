package com.mos.backend.studyschedules.application;

import com.mos.backend.common.exception.MosException;
import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studies.entity.exception.StudyErrorCode;
import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import com.mos.backend.studycurriculum.infrastructure.StudyCurriculumRepository;
import com.mos.backend.studyschedules.application.res.StudyCurriculumRes;
import com.mos.backend.studyschedules.application.res.StudyScheduleRes;
import com.mos.backend.studyschedules.entity.StudySchedule;
import com.mos.backend.studyschedules.infrastructure.StudyScheduleRepository;
import com.mos.backend.studyschedules.presentation.req.StudyScheduleCreateReq;
import com.mos.backend.studyschedules.presentation.req.StudyScheduleUpdateReq;
import com.mos.backend.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StudyScheduleService {
    private final StudyScheduleRepository studyScheduleRepository;
    private final StudyCurriculumRepository studyCurriculumRepository;
    private final EntityFacade entityFacade;

    @Transactional
    public void createStudySchedule(Long userId, Long studyId, StudyScheduleCreateReq req) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);

        saveScheduleCurriculums(req, study);

        saveStudySchedule(req, study);
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

        validateRelationalStudy(study, studySchedule.getStudy().getId());

        studySchedule.update(req.getTitle(), req.getDescription(), req.getStartDateTime(), req.getEndDateTime());
    }

    @Transactional
    public void deleteStudySchedule(Long userId, Long studyId, Long studyScheduleId) {
        User user = entityFacade.getUser(userId);
        Study study = entityFacade.getStudy(studyId);
        StudySchedule studySchedule = entityFacade.getStudySchedule(studyScheduleId);

        validateRelationalStudy(study, studySchedule.getStudy().getId());

        studyScheduleRepository.delete(studySchedule);
    }

    private void saveScheduleCurriculums(StudyScheduleCreateReq req, Study study) {
        req.getCurriculumIds().forEach((curriculumId) -> {
            StudyCurriculum studyCurriculum = entityFacade.getStudyCurriculum(curriculumId);
            validateRelationalStudy(study, studyCurriculum.getStudy().getId());
            studyCurriculumRepository.save(studyCurriculum);
        });
    }

    private void saveStudySchedule(StudyScheduleCreateReq req, Study study) {
        StudySchedule studySchedule = StudySchedule.create(
                study, req.getTitle(), req.getDescription(), req.getStartDateTime(), req.getEndDateTime()
        );

        studyScheduleRepository.save(studySchedule);
    }

    public void validateRelationalStudy(Study study, Long studyId) {
        if (!study.isRelated(studyId))
            throw new MosException(StudyErrorCode.UNRELATED_STUDY);
    }
}
