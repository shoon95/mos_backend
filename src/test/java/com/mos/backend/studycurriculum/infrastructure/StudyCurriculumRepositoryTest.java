package com.mos.backend.studycurriculum.infrastructure;

import com.mos.backend.common.EntitySaver;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studycurriculum.entity.StudyCurriculum;
import com.mos.backend.studyschedulecurriculums.entity.StudyScheduleCurriculum;
import com.mos.backend.studyschedules.entity.StudySchedule;
import com.mos.backend.testconfig.AbstractTestContainer;
import com.mos.backend.users.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class StudyCurriculumRepositoryTest extends AbstractTestContainer {
    @Autowired
    private EntitySaver entitySaver;
    @Autowired
    private StudyCurriculumRepository studyCurriculumRepository;

    @DirtiesContext
    @Test
    void findAllByStudyScheduleId() {
        // Given
        User user = entitySaver.saveUser();
        Study study = entitySaver.saveStudy();
        StudySchedule studySchedule1 = entitySaver.saveStudySchedule(study);
        StudyCurriculum studyCurriculum1 = entitySaver.saveStudyCurriculum(study);
        StudyCurriculum studyCurriculum2 = entitySaver.saveStudyCurriculum(study);
        StudyScheduleCurriculum studyScheduleCurriculum1 = entitySaver.saveStudyScheduleCurriculum(studySchedule1, studyCurriculum1);
        StudyScheduleCurriculum studyScheduleCurriculum2 = entitySaver.saveStudyScheduleCurriculum(studySchedule1, studyCurriculum2);

        // 조회하지 않는 데이터
        StudySchedule studySchedule2 = entitySaver.saveStudySchedule(study);
        StudyCurriculum studyCurriculum3 = entitySaver.saveStudyCurriculum(study);
        StudyScheduleCurriculum studyScheduleCurriculum3 = entitySaver.saveStudyScheduleCurriculum(studySchedule2, studyCurriculum3);

        // When
        List<StudyCurriculum> studyCurriculums = studyCurriculumRepository.findAllByStudyScheduleId(studySchedule1.getId());

        // Then
        assertThat(studyCurriculums).hasSize(2);
        assertThat(studyCurriculums).containsExactlyInAnyOrder(studyCurriculum1, studyCurriculum2);
    }
}
