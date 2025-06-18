package com.mos.backend.studyschedulecurriculums.application;

import com.mos.backend.common.infrastructure.EntityFacade;
import com.mos.backend.studies.application.StudyService;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import com.mos.backend.studyschedulecurriculums.entity.StudyScheduleCurriculum;
import com.mos.backend.studyschedulecurriculums.infrastructure.StudyScheduleCurriculumRepository;
import com.mos.backend.studyschedules.entity.StudySchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StudyScheduleCurriculumService {

    private final EntityFacade entityFacade;
    private final StudyService studyService;
    private final StudyScheduleCurriculumRepository studyScheduleCurriculumRepository;

    @PreAuthorize("@studySecurity.isLeaderOrAdmin(#studyId)")
    @Transactional
    public void saveAll(Long studyId, Long studyScheduleId, List<Long> curriculumIds) {
        Study study = entityFacade.getStudy(studyId);
        StudySchedule studySchedule = entityFacade.getStudySchedule(studyScheduleId);

        curriculumIds.forEach((curriculumId) -> {
            StudyCurriculum studyCurriculum = entityFacade.getStudyCurriculum(curriculumId);

            studyService.validateRelation(study, studyCurriculum.getStudy().getId());

            StudyScheduleCurriculum studyScheduleCurriculum = StudyScheduleCurriculum.create(studySchedule, studyCurriculum);
            studyScheduleCurriculumRepository.save(studyScheduleCurriculum);
        });
    }

    @Transactional
    public void deleteAll(Long studyId, Long studyScheduleId) {
        Study study = entityFacade.getStudy(studyId);
        StudySchedule studySchedule = entityFacade.getStudySchedule(studyScheduleId);

        studyService.validateRelation(study, studySchedule.getStudy().getId());

        studyScheduleCurriculumRepository.deleteAllByStudyScheduleId(studySchedule.getId());
    }
}
