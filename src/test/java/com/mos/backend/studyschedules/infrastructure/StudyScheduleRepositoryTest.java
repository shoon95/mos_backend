package com.mos.backend.studyschedules.infrastructure;

import com.mos.backend.common.EntitySaver;
import com.mos.backend.studies.entity.Study;
import com.mos.backend.studyschedules.infrastructure.dto.StudyScheduleWithAttendanceDto;
import com.mos.backend.testconfig.AbstractTestContainer;
import com.mos.backend.users.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class StudyScheduleRepositoryTest extends AbstractTestContainer {

    @Autowired
    private EntitySaver entitySaver;
    @Autowired
    private StudyScheduleRepository studyScheduleRepository;

    @Test
    @DisplayName("findAllByUserIdAndActivated 테스트")
    @DirtiesContext
    void findAllByUserIdAndActivatedTest() {
        // Given
        User user = entitySaver.saveUser();
        Study study1 = entitySaver.saveStudy();
        Study study2 = entitySaver.saveStudy();
        entitySaver.saveStudyMember(user, study1);
        entitySaver.saveStudyMember(user, study2);
        entitySaver.saveStudySchedule(study1);
        entitySaver.saveStudySchedule(study2);

        // When
        List<StudyScheduleWithAttendanceDto> dtos = studyScheduleRepository.findAllByUserIdAndActivated(user.getId());

        // Then
        assertThat(dtos).hasSize(2);
        assertThat(dtos)
                .extracting(dto -> dto.getStudyId())
                .containsExactlyInAnyOrder(study1.getId(), study2.getId());
    }
}
